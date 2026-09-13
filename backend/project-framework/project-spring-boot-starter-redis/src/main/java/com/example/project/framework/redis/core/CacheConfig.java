package com.example.project.framework.redis.core;

import com.github.benmanes.caffeine.cache.Caffeine;
import jakarta.annotation.Resource;
import org.redisson.api.RedissonClient;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Collections;
import java.util.Random;

@Configuration
@EnableCaching
public class CacheConfig {

    @Resource
    @Lazy
    private  RedissonClient redissonClient; // 注入 Redisson (需提前配置 Redisson)


    @Resource
    @Lazy
    private RedisConnectionFactory factory;


    /* Caffeine配置 */
    @Bean
    public CaffeineCacheManager localCacheManager() {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(Caffeine.newBuilder()
                .initialCapacity(100)
                .maximumSize(1000)        // 本地缓存最大条目数
                .expireAfterWrite(Duration.ofMinutes(5)) // 基线 TTL
                .recordStats());                      // 统计命中率
        return manager;
    }

    /* Redis配置 */
    @Bean
    public RedisCacheManager remoteCacheManager() {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(24))     // 基础TTL
                .computePrefixWith(name -> name + ":")
                .disableCachingNullValues();       // 禁止缓存null值

        return RedisCacheManager.builder(factory)
                .cacheDefaults(config)
                .withInitialCacheConfigurations(Collections.singletonMap(
                        "default",
                        config.entryTtl(Duration.ofHours(24 + new Random().nextInt(6))) // 随机TTL（24~30小时）
                ))
                .transactionAware()
                .build();
    }

    /* 组合管理器 */
    @Bean
    public LayeredCacheManager layeredCacheManager(
            CaffeineCacheManager localCacheManager,
            RedisCacheManager remoteCacheManager) {
        return new LayeredCacheManager(localCacheManager, remoteCacheManager);
    }
}
