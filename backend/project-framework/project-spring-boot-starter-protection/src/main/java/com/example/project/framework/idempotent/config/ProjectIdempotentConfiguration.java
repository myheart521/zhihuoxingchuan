package com.example.project.framework.idempotent.config;

import com.example.project.framework.idempotent.core.aop.IdempotentAspect;
import com.example.project.framework.idempotent.core.keyresolver.impl.DefaultIdempotentKeyResolver;
import com.example.project.framework.idempotent.core.keyresolver.impl.ExpressionIdempotentKeyResolver;
import com.example.project.framework.idempotent.core.keyresolver.IdempotentKeyResolver;
import com.example.project.framework.idempotent.core.keyresolver.impl.UserIdempotentKeyResolver;
import com.example.project.framework.idempotent.core.redis.IdempotentRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import com.example.project.framework.redis.config.ProjectRedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@AutoConfiguration(after = ProjectRedisAutoConfiguration.class)
public class ProjectIdempotentConfiguration {

    @Bean
    public IdempotentAspect idempotentAspect(List<IdempotentKeyResolver> keyResolvers, IdempotentRedisDAO idempotentRedisDAO) {
        return new IdempotentAspect(keyResolvers, idempotentRedisDAO);
    }

    @Bean
    public IdempotentRedisDAO idempotentRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new IdempotentRedisDAO(stringRedisTemplate);
    }

    // ========== 各种 IdempotentKeyResolver Bean ==========

    @Bean
    public DefaultIdempotentKeyResolver defaultIdempotentKeyResolver() {
        return new DefaultIdempotentKeyResolver();
    }

    @Bean
    public UserIdempotentKeyResolver userIdempotentKeyResolver() {
        return new UserIdempotentKeyResolver();
    }

    @Bean
    public ExpressionIdempotentKeyResolver expressionIdempotentKeyResolver() {
        return new ExpressionIdempotentKeyResolver();
    }

}
