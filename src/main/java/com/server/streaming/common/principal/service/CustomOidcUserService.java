package com.server.streaming.common.principal.service;


import com.server.streaming.common.principal.PrincipalUser;
import com.server.streaming.common.principal.converter.ProviderUserConverter;
import com.server.streaming.common.principal.converter.ProviderUserRequest;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.service.member.MemberService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

@Service
public class CustomOidcUserService extends AbstractOAuth2UserService implements
        OAuth2UserService<OidcUserRequest, OidcUser>{

    public CustomOidcUserService(MemberService memberService, ProviderUserConverter<ProviderUserRequest,
                ProviderUser> providerUserConverter) {
        super(memberService, providerUserConverter);
    }

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {

        ClientRegistration clientRegistration = ClientRegistration
                .withClientRegistration(userRequest.getClientRegistration())
                .userNameAttributeName("sub")
                .build();

        OidcUserRequest oidcUserRequest =
                new OidcUserRequest(clientRegistration, userRequest.getAccessToken(),
                        userRequest.getIdToken(), userRequest.getAdditionalParameters());

        OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService = new OidcUserService();
        OidcUser oidcUser = oidcUserService.loadUser(oidcUserRequest);

        ProviderUserRequest providerUserRequest = new ProviderUserRequest(clientRegistration, oidcUser);

        ProviderUser providerUser = super.providerUser(providerUserRequest);

        super.register(providerUser, oidcUserRequest);

        return new PrincipalUser(providerUser);
    }
}
