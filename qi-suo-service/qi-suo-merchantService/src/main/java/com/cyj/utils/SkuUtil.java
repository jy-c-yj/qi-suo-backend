package com.cyj.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SKU 生成工具类
 * 提供多种简单 SKU 生成策略，适用于一般业务场景。
 * 注意：生成的 SKU 不能保证 100% 绝对唯一，建议配合数据库唯一索引使用。
 */
public class SkuUtil {

    private static final String DATE_PATTERN = "yyyyMMddHHmmss";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(DATE_PATTERN);
    private static final AtomicLong SEQUENCE = new AtomicLong(1); // 用于时间戳模式的序列号

    /**
     * 基于 UUID 生成 SKU（去掉横线，共32位）
     */
    public static String generateByUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 基于 UUID 生成带前缀的 SKU
     * @param prefix 前缀（例如品牌、品类代码）
     */
    public static String generateByUuid(String prefix) {
        return prefix + "-" + generateByUuid();
    }

    /**
     * 基于时间戳 + 随机数生成 SKU
     * 格式：yyyyMMddHHmmss + 4位随机数
     */
    public static String generateByTimestamp() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        int random = ThreadLocalRandom.current().nextInt(1000, 9999);
        return timestamp + random;
    }

    /**
     * 基于时间戳 + 随机数生成带前缀的 SKU
     */
    public static String generateByTimestamp(String prefix) {
        return prefix + "-" + generateByTimestamp();
    }

    /**
     * 基于时间戳 + 递增序列生成 SKU（适用于单机高并发）
     * 格式：yyyyMMddHHmmss + 6位序列号（不足补零）
     * 注意：序列号基于当前时间秒重置，需自行实现重置逻辑，此处简化使用 AtomicLong 无限递增
     */
    public static String generateBySequence() {
        String timestamp = LocalDateTime.now().format(DATE_FORMATTER);
        long seq = SEQUENCE.getAndIncrement() % 1000000; // 最多6位
        return timestamp + String.format("%06d", seq);
    }

    /**
     * 基于雪花算法生成 SKU（简化版，使用 Twitter Snowflake 思想）
     * 需要数据中心ID和机器ID，生成64位数字，转为字符串
     * 此处提供一个简易静态实现，实际项目中可引入成熟库
     */
    public static class SnowflakeGenerator {
        private final long datacenterId;
        private final long machineId;
        private long sequence = 0L;
        private long lastTimestamp = -1L;

        public SnowflakeGenerator(long datacenterId, long machineId) {
            if (datacenterId > 31 || machineId > 31) {
                throw new IllegalArgumentException("数据中心ID和机器ID必须在0-31之间");
            }
            this.datacenterId = datacenterId;
            this.machineId = machineId;
        }

        public synchronized long nextId() {
            long timestamp = System.currentTimeMillis();
            if (timestamp < lastTimestamp) {
                throw new RuntimeException("时钟回拨，拒绝生成ID");
            }
            if (timestamp == lastTimestamp) {
                sequence = (sequence + 1) & 0xFFF; // 12位序列号
                if (sequence == 0) {
                    // 当前毫秒序列耗尽，等待下一毫秒
                    while ((timestamp = System.currentTimeMillis()) <= lastTimestamp) {
                        // busy wait
                    }
                }
            } else {
                sequence = 0L;
            }
            lastTimestamp = timestamp;
            // 时间戳部分（41位）+ 数据中心（5位）+ 机器（5位）+ 序列（12位）
            return ((timestamp - 1288834974657L) << 22)
                    | (datacenterId << 17)
                    | (machineId << 12)
                    | sequence;
        }
    }

//    // 示例：使用 Snowflake 生成字符串 SKU
//    public static String generateBySnowflake(SnowflakeGenerator generator) {
//        return String.valueOf(generator.nextId());
//    }
//
//    // 主方法演示
//    public static void main(String[] args) {
//        System.out.println("UUID SKU: " + generateByUuid());
//        System.out.println("带前缀UUID: " + generateByUuid("CLO"));
//        System.out.println("时间戳SKU: " + generateByTimestamp());
//        System.out.println("带前缀时间戳: " + generateByTimestamp("SHO"));
//        System.out.println("序列SKU: " + generateBySequence());
//
//        SnowflakeGenerator sg = new SnowflakeGenerator(1, 1);
//        System.out.println("雪花SKU: " + generateBySnowflake(sg));
//    }
}
