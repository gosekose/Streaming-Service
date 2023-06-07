package com.server.streaming.controller.util;

import com.server.streaming.controller.dto.request.FormRegisterRequest;
import com.server.streaming.controller.dto.request.LoginFacadeRequest;
import com.server.streaming.controller.dto.request.LoginRequest;
import com.server.streaming.service.dto.FormRegisterUserDto;
import com.server.streaming.service.dto.LoginDto;
import org.springframework.stereotype.Component;

@Component
public class RequestMapperFactory {

    public static FormRegisterUserDto mapper(FormRegisterRequest request) {
        return FormRegisterUserDto.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .username(request.getUsername())
                .build();
    }

    public static LoginFacadeRequest mapper(LoginRequest request, String remoteAddr) {
        return LoginFacadeRequest.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .remoteAddr(remoteAddr)
                .build();
    }

    public static LoginDto mapper(LoginRequest request) {
        return new LoginDto(request.getEmail(), request.getPassword());
    }

    public static LoginDto mapper(LoginFacadeRequest request) {
        return new LoginDto(request.getEmail(), request.getPassword());
    }
}
