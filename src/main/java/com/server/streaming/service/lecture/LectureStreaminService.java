package com.server.streaming.service.lecture;

import com.server.streaming.repository.rdbms.LectureListRepository;
import com.server.streaming.repository.rdbms.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureStreaminService {

    private final LectureRepository lectureRepository;
    private final LectureListRepository lectureListRepository;




}
