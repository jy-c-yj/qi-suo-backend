package com.cyj.LogMqConstants;

public final class LogMqConstants {
    private LogMqConstants() {}
    /** 主交换机 */
    public static final String LOG_EXCHANGE = "qi-suo.log.exchange";
    /** 主队列 */
    public static final String LOG_QUEUE = "qi-suo.log.queue";
    /** 主路由键 */
    public static final String LOG_ROUTING_KEY = "log.request";
    /** 死信交换机 */
    public static final String LOG_DLX = "qi-suo.log.dlx";
    /** 死信队列 */
    public static final String LOG_DLQ = "qi-suo.log.dlq";
    /** 死信路由键 */
    public static final String LOG_DEAD_ROUTING_KEY = "log.dead";
}