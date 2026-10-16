package com.cyj.config;


import com.cyj.constant.OrderMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MqConfig {

    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(OrderMqConstants.EXCHANGE, true, false);
    }

    /**
     * 延迟队列
     */
    @Bean
    public Queue orderDelayQueue(OrderProperties orderProperties) {
        // x-message-ttl 是 AMQP int 字段，Duration 被配成超大值时会溢出
        int ttlMillis = (int) Math.min(orderProperties.getTimeout().toMillis(), Integer.MAX_VALUE);
        return QueueBuilder.durable(OrderMqConstants.DELAY_QUEUE)
                .withArgument("x-message-ttl", ttlMillis)
                .withArgument("x-dead-letter-exchange", OrderMqConstants.DLX)//超时交给qi-suo.order.dlx死信交换机
                .withArgument("x-dead-letter-routing-key", OrderMqConstants.CANCEL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding orderDelayBinding(Queue orderDelayQueue, DirectExchange orderExchange) {
        return BindingBuilder.bind(orderDelayQueue).to(orderExchange).with(OrderMqConstants.DELAY_ROUTING_KEY);
    }

    /**
     *超时死信交换机
     * @return
     */
    @Bean
    public DirectExchange orderDlx() {
        return new DirectExchange(OrderMqConstants.DLX, true, false);
    }

    /**
     * 超时取消队列，消费者从这拿超时订单
     * @return
     */
    @Bean
    public Queue orderCancelQueue() {
        return QueueBuilder.durable(OrderMqConstants.CANCEL_QUEUE)
                .withArgument("x-dead-letter-exchange", OrderMqConstants.FAIL_DLX)//处理失败的消息投入最终交换机
                .withArgument("x-dead-letter-routing-key", OrderMqConstants.FAIL_ROUTING_KEY)
                .build();
    }

    @Bean
    public Binding orderCancelBinding(Queue orderCancelQueue, DirectExchange orderDlx) {
        return BindingBuilder.bind(orderCancelQueue).to(orderDlx).with(OrderMqConstants.CANCEL_ROUTING_KEY);
    }

    @Bean
    public DirectExchange orderFailDlx() {
        return new DirectExchange(OrderMqConstants.FAIL_DLX, true, false);
    }

    @Bean
    public Queue orderCancelDlq() {
        return QueueBuilder.durable(OrderMqConstants.FAIL_QUEUE).build();
    }

    @Bean
    public Binding orderCancelDlqBinding(Queue orderCancelDlq, DirectExchange orderFailDlx) {
        return BindingBuilder.bind(orderCancelDlq).to(orderFailDlx).with(OrderMqConstants.FAIL_ROUTING_KEY);
    }
}
