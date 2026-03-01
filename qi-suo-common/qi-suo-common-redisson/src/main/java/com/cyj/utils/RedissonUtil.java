package com.cyj.utils;

import org.redisson.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * Redisson 工具类
 * 提供分布式锁、缓存、原子操作等功能
 */
@Component
public class RedissonUtil {

    private static final Logger log = LoggerFactory.getLogger(RedissonUtil.class);

    private final RedissonClient redissonClient;

    @Autowired
    public RedissonUtil(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    // ==================== 分布式锁相关 ====================

    /**
     * 获取分布式锁对象
     */
    public RLock getLock(String lockKey) {
        return redissonClient.getLock(lockKey);
    }

    /**
     * 获取公平锁
     */
    public RLock getFairLock(String lockKey) {
        return redissonClient.getFairLock(lockKey);
    }

    /**
     * 获取读写锁
     */
    public RReadWriteLock getReadWriteLock(String lockKey) {
        return redissonClient.getReadWriteLock(lockKey);
    }

    /**
     * 尝试获取锁（默认超时时间）
     */
    public boolean tryLock(String lockKey) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock(3, 30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 尝试获取锁（自定义超时时间）
     */
    public boolean tryLock(String lockKey, long waitTime, long leaseTime, TimeUnit unit) {
        RLock lock = getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("获取锁失败: {}", lockKey, e);
            return false;
        }
    }

    /**
     * 释放锁
     */
    public void unlock(String lockKey) {
        RLock lock = getLock(lockKey);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }

    /**
     * 执行分布式锁保护的业务逻辑
     */
    public <T> T executeWithLock(String lockKey, long waitTime, long leaseTime,
                                 TimeUnit unit, Supplier<T> supplier) {
        RLock lock = getLock(lockKey);
        try {
            if (lock.tryLock(waitTime, leaseTime, unit)) {
                try {
                    return supplier.get();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();
                    }
                }
            } else {
                throw new RuntimeException("获取锁失败: " + lockKey);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("获取锁被中断: " + lockKey, e);
        }
    }

    /**
     * 执行分布式锁保护的业务逻辑（无返回值）
     */
    public void executeWithLock(String lockKey, long waitTime, long leaseTime,
                                TimeUnit unit, Runnable runnable) {
        executeWithLock(lockKey, waitTime, leaseTime, unit, () -> {
            runnable.run();
            return null;
        });
    }

    // ==================== 缓存相关 ====================

    /**
     * 设置缓存值
     */
    public <T> void setValue(String key, T value) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value);
    }

    /**
     * 设置缓存值（带过期时间）
     */
    public <T> void setValue(String key, T value, long timeout, TimeUnit unit) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        bucket.set(value, timeout, unit);
    }

    /**
     * 获取缓存值
     */
    public <T> T getValue(String key) {
        RBucket<T> bucket = redissonClient.getBucket(key);
        return bucket.get();
    }

    /**
     * 获取或设置默认值
     */
    public <T> T getValueOrDefault(String key, T defaultValue) {
        T value = getValue(key);
        return value != null ? value : defaultValue;
    }

    /**
     * 删除缓存
     */
    public boolean delete(String key) {
        return redissonClient.getBucket(key).delete();
    }

    /**
     * 判断key是否存在
     */
    public boolean exists(String key) {
        return redissonClient.getBucket(key).isExists();
    }

    /**
     * 设置过期时间
     */
    public boolean expire(String key, long timeout, TimeUnit unit) {
        return redissonClient.getBucket(key).expire(Duration.ofMillis(unit.toMillis(timeout)));
    }

    // ==================== Map相关操作 ====================

    /**
     * 获取Map
     */
    public <K, V> RMap<K, V> getMap(String mapName) {
        return redissonClient.getMap(mapName);
    }

    /**
     * 设置Map值
     */
    public <K, V> void putMapValue(String mapName, K key, V value) {
        RMap<K, V> map = getMap(mapName);
        map.put(key, value);
    }

    /**
     * 获取Map值
     */
    public <K, V> V getMapValue(String mapName, K key) {
        RMap<K, V> map = getMap(mapName);
        return map.get(key);
    }

    // ==================== Set相关操作 ====================

    /**
     * 获取Set
     */
    public <T> RSet<T> getSet(String setName) {
        return redissonClient.getSet(setName);
    }

    /**
     * 添加到Set
     */
    public <T> boolean addToSet(String setName, T value) {
        RSet<T> set = getSet(setName);
        return set.add(value);
    }

    /**
     * 从Set中移除
     */
    public <T> boolean removeFromSet(String setName, T value) {
        RSet<T> set = getSet(setName);
        return set.remove(value);
    }

    // ==================== List相关操作 ====================

    /**
     * 获取List
     */
    public <T> RList<T> getList(String listName) {
        return redissonClient.getList(listName);
    }



    /**
     * 添加到List
     */
    public <T> boolean addToList(String listName, T value) {
        RList<T> list = getList(listName);
        return list.add(value);
    }
    /**
     * 批量添加元素到List
     * @param listName 列表名称
     * @param values 要添加的元素集合
     * @return 是否添加成功
     */
    public <T> boolean addAllToList(String listName, Collection<T> values) {
        RList<T> list = getList(listName);
        return list.addAll(values);
    }

    // ==================== 原子操作 ====================

    /**
     * 获取原子Long
     */
    public RAtomicLong getAtomicLong(String name) {
        return redissonClient.getAtomicLong(name);
    }

    /**
     * 原子递增
     */
    public long increment(String key) {
        RAtomicLong atomicLong = getAtomicLong(key);
        return atomicLong.incrementAndGet();
    }

    /**
     * 原子递减
     */
    public long decrement(String key) {
        RAtomicLong atomicLong = getAtomicLong(key);
        return atomicLong.decrementAndGet();
    }

    // ==================== 布隆过滤器 ====================

    /**
     * 获取布隆过滤器
     */
    public <T> RBloomFilter<T> getBloomFilter(String name) {
        return redissonClient.getBloomFilter(name);
    }

    /**
     * 初始化布隆过滤器
     */
    public <T> void initBloomFilter(String name, long expectedInsertions, double falseProbability) {
        RBloomFilter<T> bloomFilter = getBloomFilter(name);
        bloomFilter.tryInit(expectedInsertions, falseProbability);
    }

    // ==================== 发布订阅 ====================

    /**
     * 发布消息
     */
    public <T> long publish(String channel, T message) {
        RTopic topic = redissonClient.getTopic(channel);
        return topic.publish(message);
    }

    /**
     * 订阅频道
     */
//    public <T> int subscribe(String channel, MessageListener<T> listener) {
//        RTopic topic = redissonClient.getTopic(channel);
//        return topic.addListener((CharSequence) channel, (channel1, msg) ->
//                listener.onMessage(channel1.toString(), (T) msg));
//    }

    public interface MessageListener<T> {
        void onMessage(String channel, T message);
    }

    // ==================== 工具方法 ====================

    /**
     * 批量删除key
     */
    public long deleteKeys(Collection<String> keys) {
        return redissonClient.getKeys().delete(keys.toArray(new String[0]));
    }

    /**
     * 按模式删除key
     */
    public long deleteByPattern(String pattern) {
        Iterable<String> keys = redissonClient.getKeys().getKeysByPattern(pattern);
        long count = 0;
        for (String key : keys) {
            if (redissonClient.getBucket(key).delete()) {
                count++;
            }
        }
        return count;
    }

    /**
     * 获取客户端实例
     */
    public RedissonClient getClient() {
        return redissonClient;
    }

    /**
     * 关闭客户端连接
     */
    public void shutdown() {
        if (redissonClient != null && !redissonClient.isShutdown()) {
            redissonClient.shutdown();
        }
    }

    /**
     * 获取Redisson实例状态
     */
    public boolean isShutdown() {
        return redissonClient == null || redissonClient.isShutdown();
    }
}