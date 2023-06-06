package com.server.streaming.repository;

import com.server.streaming.domain.lecture.LectureList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureListRepository extends JpaRepository<LectureList, Long> {
}
