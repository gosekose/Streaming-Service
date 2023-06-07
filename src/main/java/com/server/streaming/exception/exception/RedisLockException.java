package com.server.streaming.exception.exception;

import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class RedisLockException extends CommonException {

    public RedisLockException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.REDIS_ROCK_EXCEPTION);
    }
}
