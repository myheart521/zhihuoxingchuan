package com.example.project.framework.redis.core;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.lang.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 支持分布式事务的多级缓存实现
 */
public class TransactionalLayeredCache extends LayeredCache {

    private final RedissonClient redissonClient;
    private static final long LOCK_WAIT_TIME = 500; // 锁等待时间(ms)
    private static final long LOCK_LEASE_TIME = 3000; // 锁自动释放时间(ms)

    public TransactionalLayeredCache(String name, Cache localCache, RedisCache remoteCache, RedissonClient redissonClient) {
        super(name, localCache, remoteCache);
        this.redissonClient = redissonClient;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        String lockKey = "lock:cache:" + getName() + ":" + key;
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                try {
                    super.put(key, value); // 在锁保护下执行双写
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public void evict(Object key) {
        String lockKey = "lock:cache:" + getName() + ":" + key;
        RLock lock = redissonClient.getLock(lockKey);
        
        try {
            if (lock.tryLock(LOCK_WAIT_TIME, LOCK_LEASE_TIME, TimeUnit.MILLISECONDS)) {
                try {
                    super.evict(key); // 在锁保护下执行双删
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
