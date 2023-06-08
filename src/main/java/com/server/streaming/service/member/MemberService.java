package com.server.streaming.service.member;

import com.server.streaming.common.principal.social.ProviderUser;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import com.server.streaming.exception.exception.NotFoundUserException;
import com.server.streaming.exception.exception.UserRegisterConflictException;
import com.server.streaming.repository.rdbms.AuthorityRepository;
import com.server.streaming.repository.rdbms.MemberRepository;
import com.server.streaming.service.dto.FormRegisterUserDto;
import com.server.streaming.service.dto.LoginDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static com.server.streaming.domain.member.Authorities.ROLE_USER;


@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final AuthorityRepository authorityRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * findMethod
     */

    public List<Authority> findAuthorityByUserOrThrow(Member member) {
        List<Authority> authorities = authorityRepository.findAuthorityByMember(member);
        if (authorities.isEmpty()) throw new NotFoundUserException();
        return authorities;
    }

    public Member findByUserId(String userId) {
        return memberRepository.findByUserId(userId).orElseThrow(() -> {throw new NotFoundUserException();});
    }

    public Member findMemberByEmailOrThrow(LoginDto loginDto) {
        Member findUser = findMemberByEmail(loginDto.getEmail());
        if (findUser == null || !passwordEncoder.matches(loginDto.getPassword(), findUser.getPassword()))
            throw new NotFoundUserException();
        return findUser;
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional
    public boolean register(FormRegisterUserDto dto) {
        Member findMember = memberRepository.findMemberByEmail(dto.getEmail());

        if (findMember == null) {
            Member user = Member.builder()
                    .userId(UUID.randomUUID().toString())
                    .username(dto.getUsername())
                    .password(passwordEncoder.encode(dto.getPassword()))
                    .email(dto.getEmail())
                    .build();
            memberRepository.saveAndFlush(user);
            authorityRepository.saveAndFlush(Authority.builder().member(user).authorities(ROLE_USER).build());
            return true;
        }
        throw new UserRegisterConflictException();
    }

    /**
     * register
     */
    @Transactional
    public void register(String registrationId, ProviderUser providerUser) {
        Member savedMember = memberRepository.save(
                Member.builder()
                        .userId(UUID.randomUUID().toString())
                        .registrationId(registrationId)
                        .registerId(providerUser.getId())
                        .password(providerUser.getPassword())
                        .email(providerUser.getEmail())
                        .picture(providerUser.getPicture())
                        .build()
        );

        authorityRepository.save(
                Authority.builder().member(savedMember).authorities(ROLE_USER).build()
        );

    }

}
