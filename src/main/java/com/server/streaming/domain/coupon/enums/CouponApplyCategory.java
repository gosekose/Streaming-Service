package com.server.streaming.domain.coupon.enums;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum CouponApplyCategory {
    PROVIDER("강의 제공자"),
    CATEGORY("카테고리별"),
    SPECIAL("특정 강의");

    private final String name;
}
