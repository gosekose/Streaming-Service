package com.server.streaming.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.controller.dto.request.FormRegisterRequest;
import com.server.streaming.controller.dto.request.LoginFacadeRequest;
import com.server.streaming.controller.util.RequestMapperFactory;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import com.server.streaming.exception.exception.UserRegisterConflictException;
import com.server.streaming.service.dto.AuthTokenDto;
import com.server.streaming.service.loginsession.LoginSessionPolicy;
import com.server.streaming.service.member.MemberPolicy;
import com.server.streaming.service.token.TokenPolicyImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FacadeService {

    private final MemberPolicy memberPolicy;
    private final TokenPolicyImpl tokenPolicyImpl;

    private final LoginSessionPolicy loginSessionPolicy;


    /**
     * register
     */
    public void register(FormRegisterRequest request) {
        if (!memberPolicy.register(RequestMapperFactory.mapper(request))) {
            throw new UserRegisterConflictException();
        }
    }

    /**
     * login
     */
    public AuthTokenDto login(LoginFacadeRequest request) throws JsonProcessingException {
        Member member = memberPolicy.findMemberByEmailOrThrow(RequestMapperFactory.mapper(request));
        List<Authority> authorities = memberPolicy.findAuthorityByUserOrThrow(member);

        return loginSessionPolicy.loginNewSession(member, authorities, request.getRemoteAddr());
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
