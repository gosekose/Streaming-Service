package com.server.streaming.service;

import com.server.streaming.domain.member.Member;
import com.server.streaming.repository.rdbms.MemberRepository;
import com.server.streaming.service.dto.AuthTokenDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void register() {
    }

    public AuthTokenDto login() {
        return null;
    }

    public void logout() {

    }

    public Member findMemberById(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }
}
