package com.server.streaming.config;

import com.server.streaming.common.aop.RedisLockAspect;
import org.redisson.api.RedissonClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy
public class RedisAopConfig {

    @Bean
    public RedisLockAspect redisLockAspect(RedissonClient redissonClient) {
        return new RedisLockAspect(redissonClient);
    }

}
