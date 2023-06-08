package com.server.streaming.domain.discount;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor
@DiscriminatorColumn(name = "discoint_type")
public abstract class Discount extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "discount_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    public Discount(Lecture lecture) {
        this.lecture = lecture;
    }
}
