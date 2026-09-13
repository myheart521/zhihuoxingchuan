package com.example.project.framework.redis.core;

import cn.hutool.core.util.ObjectUtil;
import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.redisson.spring.cache.NullValue;
import org.springframework.cache.Cache;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * 整合本地与远程缓存，实现 “先读本地、后读远程，写时双写”
 */
public class LayeredCache implements Cache {

    private final Cache localCache;   // Caffeine 本地缓存

    private final RedisCache remoteCache; // Redis 分布式缓存
    private final String name;

    public LayeredCache(String name, Cache localCache, RedisCache remoteCache) {
        this.name = Objects.requireNonNull(name);
        this.localCache = Objects.requireNonNull(localCache);
        this.remoteCache = Objects.requireNonNull(remoteCache);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    // 先读本地缓存，再读远程缓存
    @Override
    public ValueWrapper get(Object key) {
        ValueWrapper value = localCache.get(key);
        if (value == null || value.get() instanceof NullValue) {
            value = remoteCache.get(key);
            if (value != null) {
                // 空值处理：缓存NullValue防止穿透
                Object cacheValue = (value.get() == null) ? NullValue.INSTANCE : value.get();
                
                // 回填本地缓存（有效值或空值标记）
                localCache.put(key, cacheValue);
            }
        }
        return (value != null && value.get() instanceof NullValue) ? null : value;
    }

    @Override
    public <T> T get(Object key, Class<T> type) {
        T value = localCache.get(key, type);
        if (value == null || value instanceof NullValue) {
            value = remoteCache.get(key, type);
            if (value != null) {
                Object cacheValue = (value == null) ? NullValue.INSTANCE : value;
                localCache.put(key, cacheValue);
            }
        }
        return (value instanceof NullValue) ? null : value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = localCache.get(key, valueLoader);
        if (value == null || value instanceof NullValue) {
            value = remoteCache.get(key, valueLoader);
            if (value != null) {
                Object cacheValue = (value == null) ? NullValue.INSTANCE : value;
                localCache.put(key, cacheValue);
            }
        }
        return (value instanceof NullValue) ? null : value;
    }

    // 写入时同时更新本地和远程缓存
    @Override
    public void put(Object key, @Nullable Object value) {
        localCache.put(key, value);
        remoteCache.put(key, value);
    }

    @Override
    public void evict(Object key) {
        localCache.evict(key);
        remoteCache.evict(key);
    }

    @Override
    public void clear() {
        localCache.clear();
        remoteCache.clear();
    }
}
