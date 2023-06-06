package com.server.streaming.service;

import com.server.streaming.domain.lecture.Lecture;
import com.server.streaming.repository.MemberRepository;
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
}
