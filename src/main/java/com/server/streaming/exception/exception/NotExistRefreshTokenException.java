package com.server.streaming.exception.exception;

import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class NotExistRefreshTokenException extends CommonException {

    public NotExistRefreshTokenException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.USER_NOT_REFRESHTOKEN);
    }
}
