package com.cyj.mq;


import com.cyj.constant.OrderMqConstants;
import com.cyj.service.OrderTimeoutService;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 延迟消息到期后的消费者：取消未支付订单并归还库存。
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "qi-suo.order", name = "timeout-consumer-enabled", havingValue = "true", matchIfMissing = true)
public class OrderTimeoutConsumer {

    private final MessageConverter messageConverter;
    private final OrderTimeoutService orderTimeoutService;

    @RabbitListener(queues = OrderMqConstants.CANCEL_QUEUE, containerFactory = "orderCancelListenerContainerFactory")
    public void onMessage(Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        try {
            OrderTimeoutMessage payload = (OrderTimeoutMessage) messageConverter.fromMessage(message);
            if (payload == null || payload.getOrderId() == null) {
                log.error("订单超时消息内容非法, messageId={}", message.getMessageProperties().getMessageId());
                channel.basicAck(deliveryTag, false);
                return;
            }
            boolean cancelled = orderTimeoutService.cancelUnpaidOrder(payload.getOrderId());
            log.info("订单超时消息处理完成, orderId={}, orderNo={}, cancelled={}",
                    payload.getOrderId(), payload.getOrderNo(), cancelled);
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("订单超时取消消费异常, deliveryTag={}", deliveryTag, e);
            channel.basicNack(deliveryTag, false, false); //multiple=false（只这一条）、requeue=false（不重新入队）。requeue=false 触发 broker 的死信流程
        }
    }
}
