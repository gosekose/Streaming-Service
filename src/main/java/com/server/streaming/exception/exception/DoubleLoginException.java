package com.server.streaming.exception.exception;

import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class DoubleLoginException extends CommonException {

    public DoubleLoginException() {
        super(ExceptionCode.UNAUTHORIZED, ExceptionMessage.DOUBLE_LOGIN);
    }
}
