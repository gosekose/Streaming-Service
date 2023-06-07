package com.server.streaming.exception.exception;

import com.server.streaming.exception.type.ExceptionCode;
import com.server.streaming.exception.type.ExceptionMessage;

public class UserRegisterConflictException extends CommonException {
    public UserRegisterConflictException() {
        super(ExceptionCode.CONFLICT, ExceptionMessage.USER_REGISTER_CONFLICT);
    }
}
