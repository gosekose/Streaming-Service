package com.server.streaming.domain.discount;

import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Price")
public class PriceDiscount extends Discount {
    private long amount;

    public PriceDiscount(Lecture lecture, long amount) {
        super(lecture);
        this.amount = amount;
    }
}
