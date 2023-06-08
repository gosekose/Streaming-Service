package com.server.streaming.domain.coupon;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class LectureCoupon extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "lecture_coupon_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Builder
    public LectureCoupon(Coupon coupon, Lecture lecture) {
        this.coupon = coupon;
        this.lecture = lecture;
    }
}
