package com.cyj.common;

import java.util.UUID;

/**
 * 链路追踪相关常量
 */
public final class TraceConstants {

    private TraceConstants() {
    }

    /**
     * 请求头名：网关注入，服务间 Feign 调用透传
     */
    public static final String TRACE_ID_HEADER = "trace-id";

    /**
     * MDC 键名，配合 logback-spring.xml 里的 %X{traceId:-} 输出
     */
    public static final String MDC_TRACE_ID = "traceId";

    /**
     * traceId 长度：32 位十六进制，对应 sys_oper_log.trace_id 的 CHAR(32)
     */
    public static final int TRACE_ID_LENGTH = 32;

    /**
     * 生成 traceId（32 位十六进制，即去掉横线的 UUID）
     * <p>
     * 注意与 sys_oper_log.message_id 区分：那个是 CHAR(36)，标准带横线的 UUID。
     */
    public static String generate() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
