package com.server.streaming.service.token;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.token.*;
import com.server.streaming.exception.exception.DoubleLoginException;
import com.server.streaming.exception.exception.NotExistRefreshTokenException;
import com.server.streaming.repository.redis.TokenRepositoryImpl;
import com.server.streaming.service.dto.AuthTokenDto;
import com.server.streaming.service.member.MemberService;
import com.server.streaming.service.tokenprovider.TokenProviderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TokenServiceImpl implements TokenService {

    private final TokenRepositoryImpl tokenRepository;
    private final TokenProviderService tokenProviderService;
    private final MemberService memberService;

    @Override
    public AuthTokenDto createAuthToken(String userId, List<Authority> authorities) throws JsonProcessingException {
        AuthTokenDto authTokenDto = findOrDeleteToken(userId);
        if (authTokenDto != null) {
            return authTokenDto;
        }
        return saveAndGetToken(userId, authorities);
    }


    @Override
    public AuthTokenDto createAuthToken(Authentication authentication) throws JsonProcessingException {
        String accessToken = tokenProviderService.createAccessToken(authentication);
        String newRefreshToken = createNewRefreshToken(authentication);
        return new AuthTokenDto(accessToken, newRefreshToken, getUserId(authentication));
    }

    @Override
    public void deleteAuthTokens(String accessToken, String refreshToken, String userId) throws JsonProcessingException {
        deleteTokenByType(accessToken, userId, AccessToken.class);
        deleteTokenByType(refreshToken, userId, RefreshToken.class);
    }

    @Override
    public AuthTokenDto reissueAuthToken(String refreshToken) throws JsonProcessingException {
        String userId = getUserIdOrThrow(refreshToken);
        Authentication authentication = tokenProviderService.getAuthentication(refreshToken);
        if (tokenProviderService.isMoreThanReissueTime(refreshToken))
            return AuthTokenDto.of(tokenProviderService.createAccessToken(authentication),
                    refreshToken, getUserId(authentication));

        deleteTokenByType(refreshToken, userId, RefreshToken.class);
        return AuthTokenDto.of(
                tokenProviderService.createAccessToken(authentication),
                createNewRefreshToken(authentication),
                getUserId(authentication)
        );
    }

    @Override
    public void saveLogoutTokensAndDeleteSavedTokens(String userId) throws JsonProcessingException {
        Token savedAccessToken = tokenRepository.findTokenByIdx(userId, AccessToken.class);
        Token savedRefreshToken = tokenRepository.findTokenByIdx(userId, RefreshToken.class);

        saveLogoutTokens(savedAccessToken, savedRefreshToken);
        deleteAuthTokens(savedAccessToken.getId(), savedRefreshToken.getId(), userId);
    }

    public void throwIfLogoutSessionTokens(String accessToken, String refreshToken, String userId)
            throws JsonProcessingException {

        Token logoutAccessToken = tokenRepository.findTokenByKey(accessToken, LogoutSessionAccessToken.class);
        Token logoutRefreshToken = tokenRepository.findTokenByKey(refreshToken, LogoutSessionRefreshToken.class);

        if ((logoutAccessToken != null && logoutAccessToken.getUserId().equals(userId)) ||
                (logoutRefreshToken != null && logoutRefreshToken.getUserId().equals(userId)))
            throw new DoubleLoginException();
    }

    private void saveLogoutTokens(Token savedAccessToken, Token savedRefreshToken) throws JsonProcessingException {
        tokenRepository.saveToken(savedAccessToken.getId(), mapperAccess(savedAccessToken));
        tokenRepository.saveToken(savedRefreshToken.getId(), mapperRefresh(savedRefreshToken));
    }

    private LogoutSessionAccessToken mapperAccess(Token accessToken) {
        return LogoutSessionAccessToken
                .of(accessToken.getId(), accessToken.getUserId(), accessToken.getExpiration());
    }

    private LogoutSessionRefreshToken mapperRefresh(Token refreshToken) {
        return LogoutSessionRefreshToken
                .of(refreshToken.getId(), refreshToken.getUserId(), refreshToken.getExpiration());
    }


    private AuthTokenDto findOrDeleteToken(String userId) throws JsonProcessingException {
        Token savedAccessToken = tokenRepository.findTokenByIdx(userId, AccessToken.class);
        Token savedRefreshToken = tokenRepository.findTokenByIdx(userId, RefreshToken.class);

        if (savedAccessToken != null && savedRefreshToken != null) {
            return new AuthTokenDto(savedAccessToken.getId(), savedRefreshToken.getId(), userId);
        }
        else if (savedAccessToken != null) {
            deleteTokenByType(savedAccessToken.getId(), savedAccessToken.getUserId(), AccessToken.class);
        }

        else if (savedRefreshToken != null) {
            deleteTokenByType(savedRefreshToken.getId(), savedRefreshToken.getUserId(), RefreshToken.class);
        }

        return null;
    }

    private AuthTokenDto saveAndGetToken(String userId, List<Authority> authorities) throws JsonProcessingException {
        String accessToken = tokenProviderService.createAccessToken(userId, authorities);
        String refreshToken = tokenProviderService.createRefreshToken(userId, authorities);

        saveTokens(AccessToken.of(accessToken, userId, tokenProviderService.getRemainingTimeFromToken(accessToken)),
                RefreshToken.of(refreshToken, userId, tokenProviderService.getRemainingTimeFromToken(refreshToken)));

        return new AuthTokenDto(accessToken, refreshToken, userId);
    }

    private void saveTokens(AccessToken accessToken, RefreshToken refreshToken) throws JsonProcessingException {
        tokenRepository.saveToken(accessToken.getId(), accessToken);
        tokenRepository.saveTokenIdx(accessToken.getUserId(), accessToken);
        tokenRepository.saveToken(refreshToken.getId(), refreshToken);
        tokenRepository.saveTokenIdx(refreshToken.getUserId(), refreshToken);
    }

    private String getUserIdOrThrow(String refreshToken) throws JsonProcessingException {
        Token token = tokenRepository.findTokenByKey(refreshToken, RefreshToken.class);
        if (token == null) {
            throw new NotExistRefreshTokenException();
        }
        return token.getUserId();
    }

    private String getUserId(Authentication authentication) {
        return memberService.findByUserId(authentication.getName()).getUserId();
    }

    private String createNewRefreshToken(Authentication authentication) throws JsonProcessingException {
        String newRefreshToken = tokenProviderService.createRefreshToken(authentication);

        RefreshToken refreshToken = RefreshToken.of(newRefreshToken, authentication.getName(),
                tokenProviderService.getRemainingTimeFromToken(newRefreshToken));

        tokenRepository.saveToken(refreshToken.getId(), refreshToken);

        return refreshToken.getId();
    }

    private void deleteTokenByType(String tokenId, String userId, Class<?> clazz) throws JsonProcessingException {
        tokenRepository.deleteToken(tokenId, clazz);
        tokenRepository.deleteTokenIdx(userId, clazz);
    }
}
