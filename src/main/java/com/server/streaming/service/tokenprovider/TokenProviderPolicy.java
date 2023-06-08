package com.server.streaming.service.tokenprovider;

import com.server.streaming.domain.member.Authority;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface TokenProviderPolicy {

    String TOKEN_TYPE = "Bearer ";

    String createAccessToken(Authentication authentication);

    String createRefreshToken(Authentication authentication);

    String createAccessToken(String userId, List<Authority> roles);

    String createRefreshToken(String userId, List<Authority> roles);

    Claims getClaims(String token);

    String getUserIdFromToken(String token);

    long getRemainingTimeFromToken(String token);

    boolean isMoreThanReissueTime(String token);

    Authentication getAuthentication(String token);

    boolean validateToken(String authToken, String userId);

    String removeType(String token);

    Long getRemainTime(String token);

    String createToken(Authentication authentication, long tokenTime);

    String createToken(String userId, List<Authority> authorities, long tokenTime);


}