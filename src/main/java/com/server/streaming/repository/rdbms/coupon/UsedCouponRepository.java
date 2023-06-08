package com.server.streaming.repository.rdbms.coupon;

import com.server.streaming.domain.coupon.UsedCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsedCouponRepository extends JpaRepository<UsedCoupon, Long> {
}
