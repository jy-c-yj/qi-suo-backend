package com.cyj.mq;


import com.cyj.constant.OrderMqConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * 延迟消息投递。
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OrderTimeoutPublisher {

    private final RabbitTemplate rabbitTemplate;

    /**
     * 注册一个「提交后执行」的回调。
     */
    public void afterCommit(Runnable action) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    action.run();
                }
            });
        } else {
            action.run();
        }
    }

    public void publish(Long orderId, String orderNo) {
        try {
            rabbitTemplate.convertAndSend(OrderMqConstants.EXCHANGE, OrderMqConstants.DELAY_ROUTING_KEY,
                    new OrderTimeoutMessage(orderId, orderNo));
            log.info("订单超时延迟消息已投递, orderId={}, orderNo={}", orderId, orderNo);
        } catch (Exception e) {
            // 投递失败不能让已经提交的订单对外变成异常；扫描任务会在 1 分钟内兜底
            log.error("订单超时延迟消息投递失败, orderId={}", orderId, e);
        }
    }
}
