package com.server.streaming.common.principal.service;

import com.server.streaming.common.principal.PrincipalUser;
import com.server.streaming.common.principal.converter.ProviderUserConverter;
import com.server.streaming.common.principal.converter.ProviderUserRequest;
import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.domain.member.Member;
import com.server.streaming.service.member.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService extends AbstractOAuth2UserService implements UserDetailsService {

    public CustomUserDetailsService(MemberService memberService, ProviderUserConverter<ProviderUserRequest,
                ProviderUser> providerUserConverter) {
        super(memberService, providerUserConverter);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = getMemberService().findMemberByEmail(email);

        if (member == null) {
            throw new UsernameNotFoundException("존재하지 않는 회원입니다.");
        }


        // converter 처리
        ProviderUserRequest providerUserRequest = new ProviderUserRequest(member);
        ProviderUser providerUser = providerUser(providerUserRequest);

        return new PrincipalUser(providerUser);

    }



}

