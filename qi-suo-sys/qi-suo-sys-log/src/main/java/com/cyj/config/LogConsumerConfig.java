package com.cyj.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 日志队列监听器容器工厂。
 */
@Configuration
@ConditionalOnProperty(prefix = "qi-suo.log", name = "consumer-enabled", havingValue = "true")
public class LogConsumerConfig {

    @Value("${qi-suo.log.concurrency:1}")
    private int concurrency;

    @Value("${qi-suo.log.batch-size:100}")
    private int batchSize;

    @Value("${qi-suo.log.batch-receive-timeout-ms:1000}")
    private int batchReceiveTimeoutMs;

    @Value("${qi-suo.log.prefetch:100}")
    private int prefetch;

    //自义定监听容器
    @Bean
    public SimpleRabbitListenerContainerFactory logListenerContainerFactory(ConnectionFactory connectionFactory) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);

        //单线程
        factory.setConcurrentConsumers(concurrency);
        //批量监听，方法签名接收List
        factory.setBatchListener(true);
        //批量拉取，一次从容器拉多条
        factory.setConsumerBatchEnabled(true);
        //一批最多多少
        factory.setBatchSize(batchSize);
        //从第一条起最多等多久交付，避免低流量长时间为ack
        factory.setBatchReceiveTimeout((long) batchReceiveTimeoutMs);

        factory.setPrefetchCount(prefetch);
        //开启手动ack、nck，以便逐条确认
        factory.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        //异常消息走死信
        factory.setDefaultRequeueRejected(false);
        return factory;
    }
}
