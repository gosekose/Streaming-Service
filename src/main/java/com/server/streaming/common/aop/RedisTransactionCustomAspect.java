package com.server.streaming.common.aop;

import com.server.streaming.service.dto.AuthTokenDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
@Order(2)
@RequiredArgsConstructor
public class RedisTransactionCustomAspect {

    private final RedisConnectionFactory connectionFactory;
    private final ThreadLocal<RedisConnection> threadLocal = new ThreadLocal<>();

    private RedisConnection getRedisConnection() {
        RedisConnection redisConnection = threadLocal.get();
        if (redisConnection == null) {
            redisConnection = connectionFactory.getConnection();
            threadLocal.set(redisConnection);
        }

        return redisConnection;
    }

    @Around("com.server.streaming.common.aop.Pointcuts.transactionMethod() ||" +
            "com.server.streaming.common.aop.Pointcuts.reissue()")
    public AuthTokenDto runWithAuthTokenTx(ProceedingJoinPoint joinPoint) throws Throwable {
        getRedisConnection().multi();

        try {
            Object proceed = joinPoint.proceed();
            if (proceed != null) {
                return (AuthTokenDto) proceed;
            }
        } catch (Throwable throwable) {
            throw throwable;
        }
        return null;
    }


    @Around("com.server.streaming.common.aop.Pointcuts.deleteAuthToken()")
    public void runWithDeleteTokenTx(ProceedingJoinPoint joinPoint) throws Throwable {
        getRedisConnection().multi();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    @AfterReturning(
            "com.server.streaming.common.aop.Pointcuts.transactionMethod() ||" +
            "com.server.streaming.common.aop.Pointcuts.reissue() ||" +
            "com.server.streaming.common.aop.Pointcuts.deleteAuthToken()"
    )
    public void commitTx() {
        try {
            getRedisConnection().exec();
        } catch (Exception e) {
            getRedisConnection().discard();
        } finally {
            threadLocal.remove();
        }
    }

    @AfterThrowing(
            "com.server.streaming.common.aop.Pointcuts.transactionMethod() ||" +
            "com.server.streaming.common.aop.Pointcuts.reissue() ||" +
            "com.server.streaming.common.aop.Pointcuts.deleteAuthToken()"
    )
    public void rollbackTx() {
        try {
            getRedisConnection().discard();
        }  catch (Exception e) {
            e.printStackTrace();
        } finally {
            threadLocal.remove();
        }
    }
}
