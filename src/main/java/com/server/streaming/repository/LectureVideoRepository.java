package com.server.streaming.repository;

import com.server.streaming.domain.lecture.LectureVideo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureVideoRepository extends JpaRepository<LectureVideo, Long> {
}
