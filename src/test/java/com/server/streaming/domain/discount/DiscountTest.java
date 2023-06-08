package com.server.streaming.domain.discount;

import com.server.streaming.domain.lecture.Lecture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DiscountTest {

    @Test
    @DisplayName("가격 할인 테스트")
    public void priceDiscount() throws Exception {
        //given
        PriceDiscount priceDiscount = new PriceDiscount(new Lecture(null, null, null, 10900L), 1000L);

        //when
        long price = priceDiscount.getDiscountedAmount();

        //then
        assertThat(price).isEqualTo(9900L);
    }

    @Test
    @DisplayName("비율 할인 테스트")
    public void rateDiscount() throws Exception {
        //given
        RateDiscount priceDiscount = new RateDiscount(new Lecture(null, null, null, 10909L), 10);

        //when
        long price = priceDiscount.getDiscountedAmount();

        //then
        assertThat(price).isEqualTo(10909 - 1090);
    }
}