package com.server.streaming.common.aop;

import liar.memberservice.member.service.dto.AuthTokenDto;
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

    @Around("")
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


    @Around("")
    public void runWithDeleteTokenTx(ProceedingJoinPoint joinPoint) throws Throwable {
        getRedisConnection().multi();
        try {
            joinPoint.proceed();
        } catch (Throwable throwable) {
            throw throwable;
        }
    }

    @AfterReturning(
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
