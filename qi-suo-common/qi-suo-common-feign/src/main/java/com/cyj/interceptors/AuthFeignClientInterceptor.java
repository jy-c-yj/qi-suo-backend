package com.cyj.interceptors;


import com.alibaba.fastjson.JSON;
import com.cyj.common.JwtClaimType;
import com.cyj.common.TraceConstants;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthFeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        // 透传链路 id：服务间调用不经网关，不在这里补一次，traceId 会在这一跳就断掉
        String traceId = MDC.get(TraceConstants.MDC_TRACE_ID);
        if (traceId != null && !traceId.isBlank()) {
            // header() 是追加语义，先清掉避免重试/重入时出现重复值
            template.removeHeader(TraceConstants.TRACE_ID_HEADER);
            template.header(TraceConstants.TRACE_ID_HEADER, traceId);
        }

        log.info("AuthFeignClientInterceptor 请求拦截器启动");
        JwtClaimType jwtClaimType = new JwtClaimType();

        jwtClaimType.setId(Long.parseLong("-1"));
        jwtClaimType.setRoles(List.of("system"));

        String json = JSON.toJSONString(jwtClaimType);

        //设置请求头
        template.header("Auth-claims", json);

    }
}
