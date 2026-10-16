package com.cyj.Aop;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.cyj.Annotation.Log;
import com.cyj.LogMqConstants.LogMqConstants;
import com.cyj.common.Result;
import com.cyj.common.TraceConstants;
import com.cyj.entity.SysOperLog;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
@Aspect
@Slf4j
public class logAop {

    private static final String X_FORWARDED_FOR = "X-Forwarded-For";
    private static final long UNKNOWN_USER_ID = -1L;
    private static final int MAX_PARAM_LEN = 4096;

    /**
     * uri / role_name 的列宽都是 VARCHAR(255)。
     * 落库端用的是 ON DUPLICATE KEY UPDATE（只忽略唯一键冲突），字段超长会抛异常整批进死信，
     * 所以必须在这里截断，而不是指望数据库帮忙。
     */
    private static final int MAX_URI_LEN = 255;
    private static final int MAX_ROLE_LEN = 255;

    /**
     * 需要脱敏的字段名（统一转小写后比对）
     */
    private static final Set<String> SENSITIVE_KEYS = Set.of(
            "password", "merchantpassword", "oldpassword", "newpassword",
            "confirmpassword", "token", "authorization", "secret");

    private static final ValueFilter MASK_FILTER = (object, name, value) ->
            (name != null && SENSITIVE_KEYS.contains(name.toLowerCase(Locale.ROOT))) ? "***" : value;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${spring.application.name:unknown}")
    private String serviceName;

    @Pointcut("@annotation(LogAnn)")
    public void logPointcut(Log LogAnn) {}

    /**
     * 三段式：进入时采集请求信息 → 执行目标方法 → 收尾时无论成败都投递日志。
     * 切面对业务必须透明：不改返回值、不吞异常、投递失败不影响业务。
     */
    @Around(value = "logPointcut(LogAnn)", argNames = "joinPoint,LogAnn")
    public Object around(ProceedingJoinPoint joinPoint, Log LogAnn) throws Throwable {
        SysOperLog operLog = new SysOperLog();
        long startNanos = System.nanoTime();

        fillRequestInfo(operLog, joinPoint, LogAnn);
        operLog.setCreateTime(LocalDateTime.now());

        try {
            Object result = joinPoint.proceed();
            fillSuccessInfo(operLog, result);
            return result;                                   // 必须原样返回，否则调用方收到 null
        } catch (Throwable e) {
            fillErrorInfo(operLog, e);
            throw e;                                         // 原样抛回，绝不吞异常
        } finally {
            operLog.setCostMs((int) ((System.nanoTime() - startNanos) / 1_000_000));
            sendLog(operLog);                                // 成功、失败都要投递
        }
    }

    /**
     * 请求维度：从 MDC、注解、HTTP 请求、SecurityContext 采集
     */
    private void fillRequestInfo(SysOperLog operLog, ProceedingJoinPoint joinPoint, Log ann) {
        operLog.setTraceId(MDC.get(TraceConstants.MDC_TRACE_ID));
        operLog.setMessageId(UUID.randomUUID().toString());
        operLog.setServiceName(serviceName);
        operLog.setModule(ann.module());
        operLog.setOperation(ann.desc());
        operLog.setRequestParam(buildRequestParam(joinPoint));

        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {                            // 定时任务 / MQ 消费者里取不到
            HttpServletRequest request = attributes.getRequest();
            operLog.setUri(truncate(request.getRequestURI(), MAX_URI_LEN));
            operLog.setHttpMethod(request.getMethod());
            operLog.setClientIp(resolveClientIp(request));
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Long userId) {
            operLog.setUserId(userId);
        } else {
            // 未登录、匿名（principal 是字符串 "anonymousUser"）、或非请求线程
            operLog.setUserId(UNKNOWN_USER_ID);
        }
        operLog.setRoleName(truncate(currentRoles(authentication), MAX_ROLE_LEN));
    }

    /**
     * 成功路径：从返回值判断业务成败
     */
    private static void fillSuccessInfo(SysOperLog operLog, Object result) {
        if (result instanceof Result<?> r && r.getCode() != null) {
            operLog.setBizCode(r.getCode());
            if (r.getCode() != 1) {
                operLog.setErrorMsg(truncate(r.getMsg(), 1024));   // 业务失败但没抛异常
            }
        } else {
            // 返回 void / PageResult / DTO：无法判断，没抛异常就算成功
            operLog.setBizCode(1);
        }
        operLog.setErrorMsg("null msg");
        operLog.setSuccess(operLog.getBizCode() == 1);
    }

    /**
     * 异常路径
     */
    private static void fillErrorInfo(SysOperLog operLog, Throwable root) {
        Throwable cause = rootCause(root);
        operLog.setBizCode(0);
        operLog.setSuccess(false);
        operLog.setErrorType(truncate(cause.getClass().getName(), 255));
        operLog.setErrorMsg(truncate(cause.getMessage(), 1024));
    }

    /**
     * 组装 request_param：按参数名配对后序列化、脱敏、截断
     */
    private static String buildRequestParam(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] names = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        Map<String, Object> params = new LinkedHashMap<>();   // 保证顺序稳定，日志好读
        for (int i = 0; i < args.length; i++) {
            Object arg = args[i];
            if (arg == null || arg instanceof ServletRequest || arg instanceof ServletResponse) {
                continue;
            }
            if (arg instanceof MultipartFile) {
                continue;                                    // 文件不能序列化，会读出整个文件内容
            }
            params.put(names != null && i < names.length ? names[i] : "arg" + i, arg);
        }
        if (params.isEmpty()) {
            return "";
        }
        return truncate(JSON.toJSONString(params, MASK_FILTER), MAX_PARAM_LEN);
    }

    /**
     * 投递 MQ：失败只记本地日志，绝不让它冒泡影响业务
     *
     */
    private void sendLog(SysOperLog operLog) {
        try {
            rabbitTemplate.convertAndSend(LogMqConstants.LOG_EXCHANGE,
                                          LogMqConstants.LOG_ROUTING_KEY, operLog);
        } catch (Exception e) {
            log.error("接口日志投递失败, traceId={}", operLog.getTraceId(), e);
        }
    }

    private static String currentRoles(Authentication authentication) {
        if (authentication == null) {
            return "";
        }
        return authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));
    }

    private static String resolveClientIp(HttpServletRequest request) {
        String xff = request.getHeader(X_FORWARDED_FOR);
        if (xff != null && !xff.isBlank()) {
            int comma = xff.indexOf(',');
            return (comma > 0 ? xff.substring(0, comma) : xff).trim();   // 取首段
        }
        return request.getRemoteAddr();
    }

    private static Throwable rootCause(Throwable e) {
        Throwable cur = e;
        while (cur.getCause() != null && cur.getCause() != cur) {
            cur = cur.getCause();
        }
        return cur;
    }

    private static String truncate(String s, int max) {
        if (s == null) return "";
        return s.length() <= max ? s : s.substring(0, max);
    }
}
