package com.server.streaming.domain.lecture;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

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
}
