package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.member.Member;
import com.server.streaming.domain.lecture.enums.PublicStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class Community extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "community_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_video_id")
    private LectureVideo lectureVideo;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member writer;

    private String title;

    @Lob
    private String content;

    @Enumerated(EnumType.STRING)
    private PublicStatus publicStatus;

    @Builder
    public Community(LectureVideo lectureVideo, Member writer, String title, String content, PublicStatus publicStatus) {
        this.lectureVideo = lectureVideo;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.publicStatus = publicStatus;
    }
}
