package com.server.streaming.repository.rdbms.member;

import com.server.streaming.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * findBy{} : Optional<>
 * find{}By{}: {} 반환
 */
@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUserId(String userId);
    Member findMemberByUserId(String userId);

    Optional<Member> findByEmail(String email);
    Member findMemberByEmail(String email);

}
