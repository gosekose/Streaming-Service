package com.server.streaming.service;

import com.server.streaming.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FindUtilService {

    /**
     * 조회를 위한 유틸성 클래스
     * find{}By{} -> null 포함
     * findBy{} -> null X
     */
    private final MemberRepository memberRepository;
    private final LectureRepository lectureRepository;
    private final LectureListRepository lectureListRepository;
    private final LectureVideoRepository lectureVideoRepository;
    private final CommunityRepository communityRepository;
    private final CommunityCommentRepository communityCommentRepository;

}
