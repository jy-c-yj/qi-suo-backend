package com.cyj.mq;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * 投递到延迟队列的消息体。
 *
 * 只放 Long / String：不要放 LocalDateTime，Jackson 的 JavaTimeModule 注册情况随版本变，
 * 是典型的「本机能跑、别人机器报错」。
 *
 * 注意：这条消息会在延迟队列里躺满整个超时时间，所以发版时不能改这个类的全限定名或字段名
 * （Jackson 默认 FAIL_ON_UNKNOWN_PROPERTIES，队列里的存量消息会反序列化失败）。
 * 真要改，先排空 qi-suo.order.delay.queue。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderTimeoutMessage implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long orderId;

    /** 仅用于日志定位 */
    private String orderNo;
}
