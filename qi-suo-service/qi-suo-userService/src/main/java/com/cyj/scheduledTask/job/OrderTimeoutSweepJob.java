package com.cyj.scheduledTask.job;


import com.cyj.service.OrderTimeoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 订单超时的兜底扫描。
 *
 * 它承担两件事：
 * 1. 主扫描——MQ 丢消息、消费者宕机期间到期的订单，靠它兜住；
 * 2. 补偿扫描——已取消但库存归还失败（商家服务不可用）的订单，靠它重试。
 *
 * 用 fixedDelay 不用 fixedRate：userService 用的是单线程调度器，
 * fixedRate 在单次执行超过周期时会堆积。
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "qi-suo.order", name = "sweep-enabled", havingValue = "true", matchIfMissing = true)
public class OrderTimeoutSweepJob {

    private final OrderTimeoutService orderTimeoutService;

    @Scheduled(fixedDelayString = "${qi-suo.order.sweep-interval-ms:60000}")
    public void sweep() {
        try {
            int cancelled = orderTimeoutService.sweepTimeoutOrders();
            if (cancelled > 0) {
                log.info("订单超时扫描：本轮取消 {} 单", cancelled);
            }
            int restored = orderTimeoutService.compensateStockRestore();
            if (restored > 0) {
                log.info("库存归还补偿：本轮成功 {} 单", restored);
            }
        } catch (Exception e) {
            // 必须自己兜住：抛出去会让 fixedDelay 任务被静默取消
            log.error("订单超时扫描异常", e);
        }
    }
}
