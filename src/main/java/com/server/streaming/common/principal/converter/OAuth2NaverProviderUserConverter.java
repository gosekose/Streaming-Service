package com.server.streaming.common.principal.converter;

import com.server.streaming.common.principal.enums.SocialType;
import com.server.streaming.common.principal.social.NaverUser;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.common.principal.util.OAuth2Utils;

public final class OAuth2NaverProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals(
                SocialType.NAVER.getSocialName()
        )) return null;

        return new NaverUser(
                OAuth2Utils.getSubAttributes(providerUserRequest.getOAuth2User(), "response"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration()
        );

    }
}
