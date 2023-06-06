package com.server.streaming.service;

import com.server.streaming.repository.LectureListRepository;
import com.server.streaming.repository.LectureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LectureStreamingService {

    private final LectureRepository lectureRepository;
    private final LectureListRepository lectureListRepository;




}
