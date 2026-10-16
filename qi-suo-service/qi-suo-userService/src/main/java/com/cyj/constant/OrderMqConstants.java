package com.cyj.constant;

/**
 * 订单超时取消的 MQ 拓扑常量。
 *
 * 这套拓扑刻意只声明在 userService 本地（不是 common-rabbitMQ）：
 * x-message-ttl 是队列声明参数，而 common-rabbitMQ 被 5 个服务全部传递依赖，
 * 放那儿会让 5 个服务各 declare 一次，TTL 一旦不一致就吃 PRECONDITION_FAILED。
 */
public final class OrderMqConstants {

    private OrderMqConstants() {
    }

    /** 生产者投递延迟消息用的交换机 */
    public static final String EXCHANGE = "qi-suo.order.exchange";

    /** 延迟队列：只有 TTL 和死信参数，没有消费者 */
    public static final String DELAY_QUEUE = "qi-suo.order.delay.queue";

    public static final String DELAY_ROUTING_KEY = "order.delay";

    /** 延迟队列过期后死信到的交换机 */
    public static final String DLX = "qi-suo.order.dlx";

    /** 真正的取消队列，消费者听这个 */
    public static final String CANCEL_QUEUE = "qi-suo.order.cancel.queue";

    public static final String CANCEL_ROUTING_KEY = "order.cancel";

    /** 取消队列自身也挂死信：处理失败的消息留痕，不无限重投（兜底是扫描任务） */
    public static final String FAIL_DLX = "qi-suo.order.cancel.fail.dlx";

    public static final String FAIL_QUEUE = "qi-suo.order.cancel.dlq";

    public static final String FAIL_ROUTING_KEY = "order.cancel.failed";
}
