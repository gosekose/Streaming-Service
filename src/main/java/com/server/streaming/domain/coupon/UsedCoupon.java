package com.server.streaming.domain.coupon;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
public class UsedCoupon extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "coupon_use_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "use_member_id")
    private Member member;

    @Builder
    public UsedCoupon(Coupon coupon, Member member) {
        this.coupon = coupon;
        this.member = member;
    }
}
