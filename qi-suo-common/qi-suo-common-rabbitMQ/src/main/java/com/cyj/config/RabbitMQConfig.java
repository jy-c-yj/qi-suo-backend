package com.cyj.config;


import com.cyj.LogMqConstants.LogMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Bean
    public MessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        converter.setCreateMessageIds(true);
        return converter;
    }
    /* ==================== 主交换机：topic, durable ==================== */
    @Bean
    public TopicExchange qiSuoLogExchange() {
        return ExchangeBuilder
                .topicExchange(LogMqConstants.LOG_EXCHANGE)
                .durable(true)
                .build();
    }
    /* ==================== 主队列：带死信参数 ==================== */
    @Bean
    public Queue qiSuoLogQueue() {
        return QueueBuilder
                .durable(LogMqConstants.LOG_QUEUE)
                .withArgument("x-dead-letter-exchange", LogMqConstants.LOG_DLX)
                .withArgument("x-dead-letter-routing-key", LogMqConstants.LOG_DEAD_ROUTING_KEY)
                .build();
    }
    /* 主绑定：log.request */
    @Bean
    public Binding qiSuoLogBinding(Queue qiSuoLogQueue, TopicExchange qiSuoLogExchange) {
        return BindingBuilder
                .bind(qiSuoLogQueue)
                .to(qiSuoLogExchange)
                .with(LogMqConstants.LOG_ROUTING_KEY);
    }
    /* 死信交换机：direct, durable */
    @Bean
    public DirectExchange qiSuoLogDlx() {
        return ExchangeBuilder
                .directExchange(LogMqConstants.LOG_DLX)
                .durable(true)
                .build();
    }
    /*死信队列*/
    @Bean
    public Queue qiSuoLogDlq() {
        return QueueBuilder
                .durable(LogMqConstants.LOG_DLQ)
                .build();
    }
    /*死信绑定：log.dead  */
    @Bean
    public Binding qiSuoLogDlqBinding(Queue qiSuoLogDlq, DirectExchange qiSuoLogDlx) {
        return BindingBuilder
                .bind(qiSuoLogDlq)
                .to(qiSuoLogDlx)
                .with(LogMqConstants.LOG_DEAD_ROUTING_KEY);
    }
}
