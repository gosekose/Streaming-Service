package com.server.streaming.domain.discount;

import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Price")
public class PriceDiscount extends Discount {
    private long amount;

    @Builder
    public PriceDiscount(Lecture lecture, long amount) {
        super(lecture);
        this.amount = amount;
    }

    @Override
    public long getDiscountedAmount() {
        Lecture lecture = super.getLecture();
        if (lecture.getPrice() >= amount) return lecture.getPrice() - amount;
        return 0L;
    }
}
