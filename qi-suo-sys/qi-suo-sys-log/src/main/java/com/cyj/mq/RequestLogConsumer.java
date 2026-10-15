package com.cyj.mq;

import com.cyj.LogMqConstants.LogMqConstants;
import com.cyj.common.TraceConstants;
import com.cyj.entity.SysOperLog;
import com.cyj.mapper.SysOperLogMapper;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 接口日志消费者
 */
@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "qi-suo.log", name = "consumer-enabled", havingValue = "true")
public class RequestLogConsumer {


    //最多重试次数
    private static final int MAX_ATTEMPTS = 3;
    //每次重试睡眠时间
    private static final long INITIAL_BACKOFF_MS = 200;

    private final SysOperLogMapper mapper;


    private final MessageConverter messageConverter;



    @RabbitListener(queues = LogMqConstants.LOG_QUEUE, containerFactory = "logListenerContainerFactory")
    public void onMessageBatch(List<Message> messages, Channel channel) {
        if (messages == null || messages.isEmpty()) {
            return;
        }
        boolean perMessage = false;
        try {
            List<SysOperLog> logs = convertAll(messages);
            if (logs != null && insertWithRetry(logs)) {
                //批量消息确认
                basicAck(channel, tagOf(messages.get(messages.size() - 1)));
                return;
            }
            //批量入库失败，开始逐条入库
            perMessage = true;
            settleOneByOne(messages, channel);
        } catch (Throwable t) {
            log.error("接口日志消费异常, size={}", messages.size(), t);
            if (!perMessage) {
                for (Message message : messages) {
                    basicNack(channel, tagOf(message));
                }
            }
        }
    }


    private List<SysOperLog> convertAll(List<Message> messages) {
        List<SysOperLog> logs = new ArrayList<>(messages.size());
        for (Message message : messages) {
            try {
                logs.add((SysOperLog) messageConverter.fromMessage(message));
            } catch (Exception e) {
                // traceId 在消息体里，body 都解不开就拿不到，只能按 deliveryTag 记
                log.error("接口日志反序列化失败, deliveryTag={}", tagOf(message), e);
                return null;
            }
        }
        return logs;
    }

    /**
     * 批量写 + 退避重试。
     */
    private boolean insertWithRetry(List<SysOperLog> logs) {

        long backoff = INITIAL_BACKOFF_MS;
        for (int attempt = 1; attempt <= MAX_ATTEMPTS; attempt++) {
            try {
                mapper.batchInsert(logs);
                return true;
            } catch (Exception e) {
                if (attempt == MAX_ATTEMPTS) {
                    log.error("接口日志批量落库失败, 已重试 {} 次, size={}", MAX_ATTEMPTS, logs.size(), e);
                    return false;
                }
                log.warn("接口日志批量落库失败, 准备第 {} 次重试, size={}, cause={}",
                         attempt + 1, logs.size(), e.toString());
                if (!sleepQuietly(backoff)) {
                    return false;              // 结束重试
                }
                backoff *= 2;
            }
        }
        return false;
    }

    /**
     * 逐条转换、逐条写库、逐条结算，每条ack或nack一次。
     */
    private void settleOneByOne(List<Message> messages, Channel channel) {
        log.warn("接口日志降级为逐条处理, size={}", messages.size());
        for (Message message : messages) {
            long tag = tagOf(message);
            SysOperLog operLog;
            try {
                operLog = (SysOperLog) messageConverter.fromMessage(message);
            } catch (Exception e) {
                log.error("接口日志反序列化失败, deliveryTag={}", tag, e);
                basicNack(channel, tag);
                continue;
            }
            try {
                mapper.batchInsert(List.of(operLog));
                basicAck(channel, tag);
            } catch (Exception e) {
                logWithTraceId(operLog, "接口日志逐条写入仍失败", e);
                basicNack(channel, tag);
            }
        }
    }



    private void logWithTraceId(SysOperLog operLog, String action, Throwable e) {
        String traceId = operLog.getTraceId();
        try {
            if (traceId != null && !traceId.isBlank()) {
                MDC.put(TraceConstants.MDC_TRACE_ID, traceId);
            }
            log.error("{}, messageId={}", action, operLog.getMessageId(), e);
        } finally {
            MDC.remove(TraceConstants.MDC_TRACE_ID);
        }
    }

    private static long tagOf(Message message) {
        return message.getMessageProperties().getDeliveryTag();
    }

    private static boolean basicAck(Channel channel, long tag) {
        try {
            channel.basicAck(tag, true);      // multiple=true：批量模式下这批的 tag 连续，一次覆盖
            return true;
        } catch (IOException e) {
            log.error("basicAck 失败, tag={}", tag, e);
            return false;
        }
    }

    private static boolean basicNack(Channel channel, long tag) {
        try {
            channel.basicNack(tag, false, false);
            return true;
        } catch (IOException e) {
            log.error("basicNack 失败, tag={}", tag, e);
            return false;
        }
    }

    /**
     * @return false 表示线程被中断，调用方应放弃剩余重试
     */
    private static boolean sleepQuietly(long millis) {
        try {
            Thread.sleep(millis);
            return true;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }
}
