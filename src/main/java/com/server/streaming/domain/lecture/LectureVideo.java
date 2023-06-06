package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class LectureVideo extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "lecture_video_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    private String title;

    private Long totalTime;
    private String videoAddr;
    private String videoOriginalName;

    @Builder
    public LectureVideo(Lecture lecture, String title, Long totalTime, String videoAddr, String videoOriginalName) {
        this.lecture = lecture;
        this.title = title;
        this.totalTime = totalTime;
        this.videoAddr = videoAddr;
        this.videoOriginalName = videoOriginalName;
    }
}
