package com.server.streaming.service.loginsession;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import com.server.streaming.domain.session.LoginSession;
import com.server.streaming.service.dto.AuthTokenDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LoginSessionService {

    AuthTokenDto loginNewSession(Member member, List<Authority> authorities, String remoteAddr)
            throws JsonProcessingException;

    LoginSession findLoginSessionByUserId(String userId) throws JsonProcessingException;

}
