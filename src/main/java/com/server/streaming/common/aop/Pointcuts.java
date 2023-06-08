package com.server.streaming.common.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Pointcut;

@Slf4j
public class Pointcuts {

    @Pointcut("execution(* com.server.streaming.service.token.TokenPolicy.deleteAuthTokens(..))")
    public void deleteAuthToken() {}

    @Pointcut("execution(* com.server.streaming.service.token.TokenPolicy.reissueAuthToken(..))")
    public void reissue() {}

    @Pointcut("@annotation(com.server.streaming.common.aop.anno.RedisTransactionalAuthTokenDto)")
    public void transactionMethod() {};

}
