package com.server.streaming.common.principal.converter;

import com.server.streaming.common.principal.enums.SocialType;
import com.server.streaming.common.principal.social.KakaoUser;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.common.principal.util.OAuth2Utils;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public final class OAuth2KakaoProviderUserConverter implements ProviderUserConverter<ProviderUserRequest, ProviderUser> {

    @Override
    public ProviderUser converter(ProviderUserRequest providerUserRequest) {

        if (!providerUserRequest.getClientRegistration().getRegistrationId().equals(SocialType.KAKAO.getSocialName())) {
            return null;
        }

        if (providerUserRequest.getOAuth2User() instanceof OidcUser) {
            return null;
        }

        return new KakaoUser(OAuth2Utils.getOtherAttributes(
                providerUserRequest.getOAuth2User(),
                "kakao_account", "profile"),
                providerUserRequest.getOAuth2User(),
                providerUserRequest.getClientRegistration());
    }
}
