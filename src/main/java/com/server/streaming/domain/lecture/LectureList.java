package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.member.Member;
import com.server.streaming.domain.lecture.enums.LectureStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class LectureList extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "lecture_list_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Enumerated(EnumType.STRING)
    private LectureStatus lectureStatus; // 강의 수강 상태

    @Builder
    public LectureList(Lecture lecture, Member member, LectureStatus lectureStatus) {
        this.lecture = lecture;
        this.member = member;
        this.lectureStatus = lectureStatus;
    }

    public void changeLectureStatus(LectureStatus lectureStatus) {
        this.lectureStatus = lectureStatus;
    }

    public boolean isPayed() { // 결제 여부 판단
        return !this.lectureStatus.equals(LectureStatus.BEFORE_PAYMENT);
    }
}
