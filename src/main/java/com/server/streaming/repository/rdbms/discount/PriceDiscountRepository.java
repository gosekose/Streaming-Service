package com.server.streaming.repository.rdbms.discount;

import com.server.streaming.domain.discount.PriceDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceDiscountRepository extends JpaRepository<PriceDiscount, Long> {
}
