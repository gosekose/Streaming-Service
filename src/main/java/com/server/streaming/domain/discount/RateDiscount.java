package com.server.streaming.domain.discount;

import com.server.streaming.domain.lecture.Lecture;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@DiscriminatorValue("Rate")
public class RateDiscount extends Discount {
    private int percent;

    public RateDiscount(Lecture lecture, int percent) {
        super(lecture);
        this.percent = percent;
    }
}
