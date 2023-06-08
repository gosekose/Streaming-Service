package com.server.streaming.service.loginsession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.common.aop.anno.RedisTransactionalAuthTokenDto;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import com.server.streaming.domain.session.LoginSession;
import com.server.streaming.repository.redis.LoginSessionRepository;
import com.server.streaming.service.dto.AuthTokenDto;
import com.server.streaming.service.token.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginSessionServiceImpl implements LoginSessionService {

    private final LoginSessionRepository loginSessionRepository;
    private final TokenService tokenService;

    @Value("${jwt.access-expiration-time}")
    private Long expiration;

    @RedisTransactionalAuthTokenDto
    @Override
    public AuthTokenDto loginNewSession(Member member, List<Authority> authorities, String remoteAddr)
            throws JsonProcessingException {

        if (isDoubleLogin(member.getUserId())) {
            logoutSession(member.getUserId());
            tokenService.saveLogoutTokensAndDeleteSavedTokens(member.getUserId());
        }
        loginSession(member.getUserId(), remoteAddr);

        return tokenService.createAuthToken(member.getUserId(), authorities);
    }

    @Override
    public LoginSession findLoginSessionByUserId(String userId) throws JsonProcessingException {
        return loginSessionRepository.findLoginSessionByUserId(userId);
    }

    private boolean isDoubleLogin(String userId) {
        return loginSessionRepository.existLoginSession(userId);
    }

    private void loginSession(String userId, String remoteAddr) throws JsonProcessingException {
        loginSessionRepository.saveLoginSession(LoginSession.of(userId, remoteAddr, expiration));
    }

    private void logoutSession(String userId) {
        loginSessionRepository.deleteLoginSession(userId);
    }
}
