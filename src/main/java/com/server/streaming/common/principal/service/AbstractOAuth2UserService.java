package com.server.streaming.common.principal.service;

import com.server.streaming.common.principal.converter.ProviderUserConverter;
import com.server.streaming.common.principal.converter.ProviderUserRequest;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.domain.member.Member;
import com.server.streaming.service.member.MemberService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;


@Service
@Slf4j
@Getter
public abstract class AbstractOAuth2UserService {

    private final MemberService memberService;
    private final ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter;

    public AbstractOAuth2UserService(MemberService memberService,
                                     ProviderUserConverter<ProviderUserRequest, ProviderUser> providerUserConverter) {
        this.memberService = memberService;
        this.providerUserConverter = providerUserConverter;
    }

    public void register(ProviderUser providerUser, OAuth2UserRequest oAuth2UserRequest) {
        Member member = memberService.findMemberByEmail(providerUser.getEmail());

        if (member == null) {
            ClientRegistration clientRegistration = oAuth2UserRequest.getClientRegistration();
            memberService.register(clientRegistration.getRegistrationId(), providerUser);
        }

    }


    public ProviderUser providerUser(ProviderUserRequest providerUserRequest) {
        return providerUserConverter.converter(providerUserRequest);
    }

}
