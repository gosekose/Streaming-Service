package com.server.streaming.repository.rdbms.discount;

import com.server.streaming.domain.discount.RateDiscount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RateDiscountRepository extends JpaRepository<RateDiscount, Long> {
}
