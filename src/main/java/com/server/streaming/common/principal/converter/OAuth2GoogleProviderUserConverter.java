package com.server.streaming.common.principal.converter;

import com.server.streaming.common.principal.enums.SocialType;
import com.server.streaming.common.principal.social.GoogleUser;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.common.principal.util.OAuth2Utils;

public final class OAuth2GoogleProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {
    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals(
                SocialType.GOOGLE.getSocialName()
        )) return null;

        return new GoogleUser(
                OAuth2Utils.getMainAttributes(providerUserRequest.getOAuth2User()),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration()
        );
    }
}
