package com.server.streaming.domain.coupon;

import com.server.streaming.domain.BaseEntity;
import com.server.streaming.domain.coupon.enums.CouponApplyCategory;
import com.server.streaming.domain.member.Member;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Table(indexes = {
        @Index(name = "coupon_seq_number_index", columnList = "seqNumber")
})
@NoArgsConstructor
public class Coupon extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "coupon_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private CouponApplyCategory couponApplyCategory;

    private String seqNumber;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "provider_member_id")
    private Member provider;

    private boolean isUsed;

    @Builder
    public Coupon(String name, CouponApplyCategory couponApplyCategory, String seqNumber, Member provider, boolean isUsed) {
        this.name = name;
        this.couponApplyCategory = couponApplyCategory;
        this.seqNumber = seqNumber;
        this.provider = provider;
        this.isUsed = isUsed;
    }

}
