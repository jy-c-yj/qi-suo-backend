package com.cyj.config;


import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 取消队列专用的监听容器工厂。
 */
@Configuration
@ConditionalOnProperty(prefix = "qi-suo.order", name = "timeout-consumer-enabled", havingValue = "true", matchIfMissing = true)
public class OrderConsumerConfig {

    @Bean
    public SimpleRabbitListenerContainerFactory orderCancelListenerContainerFactory(
            ConnectionFactory connectionFactory, OrderProperties orderProperties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(orderProperties.getConsumerConcurrency());
        factory.setPrefetchCount(orderProperties.getConsumerPrefetch());
        //手动ack
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        // 处理失败的消息进死信队列留痕
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
