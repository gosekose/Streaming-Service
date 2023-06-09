package com.server.streaming.common.principal.service;

import com.server.streaming.common.principal.PrincipalUser;
import com.server.streaming.common.principal.converter.ProviderUserConverter;
import com.server.streaming.common.principal.converter.ProviderUserRequest;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends AbstractOAuth2UserService implements
        OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    public CustomOAuth2UserService(MemberService memberService,
                                   ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        super(memberService, providerUserConverter);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oAuth2User);

        ProviderUser providerUser = super.providerUser(providerUserRequest);

        super.register(providerUser, userRequest);

        return new PrincipalUser(providerUser);

    }
}
