package com.server.streaming.exception.exception;

import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class TooManyRequestException extends CommonException {

    public TooManyRequestException() {super(ExceptionCode.TOO_MANY_REQUEST, ExceptionMessage.TOO_MANY_REQUEST);}
}
