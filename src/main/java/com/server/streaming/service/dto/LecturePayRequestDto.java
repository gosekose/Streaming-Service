package com.server.streaming.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LecturePayRequestDto {

    private String userId;
    private Long lectureId;
    private Long price;

}
