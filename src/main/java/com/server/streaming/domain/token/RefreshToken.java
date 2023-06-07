package com.server.streaming.domain.token;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class RefreshToken extends Token implements Serializable {

    private RefreshToken(String id, String userId, long expiration) {
        super(id, userId, expiration);
    }

    public static RefreshToken of (String refreshToken, String userId, Long expiration) {
        return new RefreshToken(refreshToken, userId, expiration);
    }

}
