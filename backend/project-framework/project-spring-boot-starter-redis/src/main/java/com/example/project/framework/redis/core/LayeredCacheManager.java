package com.example.project.framework.redis.core;

import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * 管理所有缓存实例，按需创建 LayeredCache
 */
public class LayeredCacheManager implements CacheManager {
    private final CaffeineCacheManager localCacheManager;
    private final RedisCacheManager remoteCacheManager;
    private final Map<String, Cache> caches = new HashMap<>();
    @Resource
    private RedissonClient redissonClient; // 注入Redisson客户端

    public LayeredCacheManager(CaffeineCacheManager localCacheManager, RedisCacheManager remoteCacheManager) {
        this.localCacheManager = localCacheManager;
        this.remoteCacheManager = remoteCacheManager;
    }

    @Override
    public Cache getCache(String name) {
        return caches.computeIfAbsent(name, cacheName -> {
            Cache localCache = localCacheManager.getCache(cacheName);
            Cache remoteCache = remoteCacheManager.getCache(cacheName);
            return new TransactionalLayeredCache(cacheName, localCache, (RedisCache) remoteCache, redissonClient);
        });
    }

    @Override
    public Collection<String> getCacheNames() {
        return Collections.unmodifiableSet(caches.keySet());
    }
}
