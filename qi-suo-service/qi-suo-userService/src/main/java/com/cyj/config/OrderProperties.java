package com.cyj.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.time.Duration;

/**
 * 订单超时取消相关配置：qi-suo.order.*
 */
@Component
@ConfigurationProperties(prefix = "qi-suo.order")
@Data
public class OrderProperties {

    /** 未支付超时时长 */
    private Duration timeout = Duration.ofMinutes(15);

    /** 超时取消消费者开关 */
    private boolean timeoutConsumerEnabled = true;

    /** 兜底扫描开关 */
    private boolean sweepEnabled = true;

    /** 扫描间隔，默认 1 分钟 */
    private long sweepIntervalMs = 60_000;

    /** 单轮扫描最多处理多少单*/
    private int sweepBatchSize = 200;

    /** 取消队列的并发消费者数 */
    private int consumerConcurrency = 1;

    private int consumerPrefetch = 1;
}
