package com.server.streaming.domain.discount;

import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Rate")
public class RateDiscount extends Discount {
    private int percent; // 소수점 버리기

    @Builder
    public RateDiscount(Lecture lecture, int percent) {
        super(lecture);
        this.percent = percent;
    }

    @Override
    public long getDiscountedAmount() {
        Lecture lecture = super.getLecture();
        long discount = lecture.getPrice() - (lecture.getPrice() / percent);
        return discount >= 0? discount : 0;
    }
}
