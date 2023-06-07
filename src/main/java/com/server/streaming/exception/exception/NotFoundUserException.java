package com.server.streaming.exception.exception;


import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class NotFoundUserException extends CommonException {

    public NotFoundUserException() {super(ExceptionCode.NOT_FOUND, ExceptionMessage.USER_NOT_FOUND);}
}
