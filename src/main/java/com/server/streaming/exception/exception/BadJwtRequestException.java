package com.server.streaming.exception.exception;


import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class BadJwtRequestException extends CommonException {

    public BadJwtRequestException() {super(ExceptionCode.BAD_REQUEST, ExceptionMessage.BAD_REQUEST);}
}
