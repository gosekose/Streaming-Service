package com.server.streaming.service.lecture;

import com.server.streaming.service.dto.LecturePayRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LecturePayService {
    // pay를 요청하기 위한 service -> pay 서버에서 pay가 인증되면 강의 수강 가능

    /**
     * userId, lectureId, price를 받아서 dto 반환
     */
    public LecturePayRequestDto requestPayToServer(String userId, Long lectureId, Long price) {
        return new LecturePayRequestDto(userId, lectureId, price);
    }

    public void responsePayFromServer() {

    }

}
