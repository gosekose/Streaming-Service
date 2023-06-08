package com.server.streaming.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.controller.dto.request.FormRegisterRequest;
import com.server.streaming.controller.dto.request.LoginFacadeRequest;
import com.server.streaming.controller.util.RequestMapperFactory;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import com.server.streaming.exception.exception.UserRegisterConflictException;
import com.server.streaming.service.dto.AuthTokenDto;
import com.server.streaming.service.loginsession.LoginSessionService;
import com.server.streaming.service.member.MemberService;
import com.server.streaming.service.token.TokenServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacadeService {

    private final MemberService memberService;
    private final TokenServiceImpl tokenPolicyImpl;

    private final LoginSessionService loginSessionService;


    /**
     * register
     */
    public void register(FormRegisterRequest request) {
        if (!memberService.register(RequestMapperFactory.mapper(request))) {
            throw new UserRegisterConflictException();
        }
    }

    /**
     * login
     */
    public AuthTokenDto login(LoginFacadeRequest request) throws JsonProcessingException {
        Member member = memberService.findMemberByEmailOrThrow(RequestMapperFactory.mapper(request));
        List<Authority> authorities = memberService.findAuthorityByUserOrThrow(member);

        return loginSessionService.loginNewSession(member, authorities, request.getRemoteAddr());
    }

    /**
     * login
     */
    public AuthTokenDto login(Authentication authentication) throws JsonProcessingException {
        return tokenPolicyImpl.createAuthToken(authentication);
    }

    /**
     * logout
     */
    public void logout(String accessToken, String refreshToken, String userId) throws JsonProcessingException {
        tokenPolicyImpl.deleteAuthTokens(accessToken, refreshToken, userId);
    }

    /**
     * reissue
     */
    public AuthTokenDto reissue(String refreshToken) throws JsonProcessingException {
        return tokenPolicyImpl.reissueAuthToken(refreshToken);
    }

}
