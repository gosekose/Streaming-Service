package com.server.streaming.controller.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.domain.token.*;
import com.server.streaming.exception.exception.BadJwtRequestException;
import com.server.streaming.repository.redis.LoginSessionRepository;
import com.server.streaming.repository.redis.TokenRepository;
import com.server.streaming.service.tokenprovider.TokenProviderPolicyImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.PatternMatchUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class AuthenticationGiveFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String REFRESHTOKEN_HEADER = "RefreshToken";
    private static final String USER_ID_HEADER = "UserId";

    private final TokenProviderPolicyImpl tokenProvider;
    private final TokenRepository tokenRepository;
    private final LoginSessionRepository loginSessionRepository;

    private static final String[] whitelist = {
            "/",
            "/static/**",
            "/favicon.ico",
            "/member-service/login",
            "/member-service/register",
            "/member-service/test",
            "/member-service/csrf",
            "/oauth2/authorization/google",
            "/oauth2/authorization/naver",
            "/oauth2/authorization/kakao"
    };


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String requestURI = request.getRequestURI();


        if (isAuthorizationIssueRequired(requestURI)) {
            validateAuthorizationHeaders(request);
            SecurityContextHolder.getContext()
                    .setAuthentication(tokenProvider.getAuthentication(getJwtIfValidateRequestHeader(request)));
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthorizationIssueRequired(String requestURI) {
        return !PatternMatchUtils.simpleMatch(whitelist, requestURI);
    }

    private void validateAuthorizationHeaders(HttpServletRequest request) {
        hasAuthorizationHeader(request);
        hasRefreshToken(request);
        hasUserIdHeader(request);
    }

    private String getJwtIfValidateRequestHeader(HttpServletRequest request) throws JsonProcessingException {
        String accessToken = parseAccessToken(request);
        String refreshToken = parseRefreshToken(request);
        String userId = parseUserId(request);

        return getJwtIfValidateRequestHeader(accessToken, refreshToken, userId);
    }

    private String getJwtIfValidateRequestHeader(String accessToken, String refreshToken, String userId) throws JsonProcessingException {
        if (StringUtils.hasText(accessToken)
                && validateToken(accessToken, userId)
                && validateToken(refreshToken, userId)
                && existsToken(accessToken, refreshToken)
                && isLoginSession(userId)) return accessToken;
        return null;
    }

    private void hasAuthorizationHeader(HttpServletRequest request) {
        if (request.getHeader(AUTHORIZATION_HEADER) == null || request.getHeader(AUTHORIZATION_HEADER).equals(""))
            throw new BadJwtRequestException();
    }

    /**
     * userId 헤더 포함 및 header List empty 여부 확인
     */
    private void hasUserIdHeader(HttpServletRequest request) {
        if (request.getHeader(USER_ID_HEADER) == null || request.getHeader(USER_ID_HEADER).equals(""))
            throw new BadJwtRequestException();
    }

    /**
     * RefreshToken 헤더 포함 및 header List empty 여부 확인
     */
    private void hasRefreshToken(HttpServletRequest request) {
        if (request.getHeader(REFRESHTOKEN_HEADER) == null || request.getHeader(REFRESHTOKEN_HEADER).equals(""))
            throw new BadJwtRequestException();
    }


    /**
     * request 요청에서 token 파싱
     */
    private String parseAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) return bearerToken.substring(7);
        return null;
    }


    private String parseRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESHTOKEN_HEADER);
    }

    /**
     * request 요청에서 userId 파싱
     */
    private String parseUserId(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    private boolean existsToken(String jwt, Class<?> clazz) throws JsonProcessingException {
        return tokenRepository.findTokenByKey(jwt, clazz) != null;
    }

    private boolean existsToken(String accessToken, String refreshToken) throws JsonProcessingException {
        return existsToken(accessToken, AccessToken.class) && existsToken(refreshToken, RefreshToken.class)
                && !existsToken(accessToken, LogoutSessionAccessToken.class)
                && !existsToken(refreshToken, LogoutSessionRefreshToken.class);
    }

    private boolean isLoginSession(String userId) throws JsonProcessingException {
        return loginSessionRepository.existLoginSession(userId);
    }

    private boolean validateToken(String token, String userId) {
        return tokenProvider.validateToken(token, userId);
    }
}
