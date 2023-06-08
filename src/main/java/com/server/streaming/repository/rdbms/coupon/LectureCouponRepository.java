package com.server.streaming.repository.rdbms.coupon;

import com.server.streaming.domain.coupon.LectureCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureCouponRepository extends JpaRepository<LectureCoupon, Long> {
}
