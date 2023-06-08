package com.server.streaming.repository.rdbms.member;

import com.server.streaming.domain.member.Authority;
import com.server.streaming.domain.member.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, String> {

    @EntityGraph(attributePaths = "member")
    List<Authority> findAuthorityByMember(Member member);
}
