package com.server.streaming.repository.rdbms.community;

import com.server.streaming.domain.lecture.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommunityCommentRepository extends JpaRepository<CommunityComment, Long> {
}
