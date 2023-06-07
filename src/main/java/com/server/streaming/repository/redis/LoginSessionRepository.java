package com.server.streaming.repository.redis;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.server.streaming.domain.session.LoginSession;
import org.springframework.stereotype.Repository;

@Repository
public interface LoginSessionRepository {

    void saveLoginSession(LoginSession loginSession) throws JsonProcessingException;
    boolean existLoginSession(String userId);
    void deleteLoginSession(String userId);
    LoginSession findLoginSessionByUserId(String userId) throws JsonProcessingException;

}
