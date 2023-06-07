package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.*;

@Entity
@Getter
@NoArgsConstructor
public class Lecture extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "lecture_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member provider;

    private String lectureName;

    @Lob
    private String description;

    private Long price; // 가격이 0원인 경우 무료강의

    @Builder
    public Lecture(Member provider, String lectureName, String description, Long price) {
        this.provider = provider;
        this.lectureName = lectureName;
        this.description = description;
        this.price = price;
    }
}
