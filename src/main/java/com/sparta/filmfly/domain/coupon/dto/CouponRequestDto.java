package com.sparta.filmfly.domain.coupon.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class CouponRequestDto {

    @NotBlank(message = "쿠폰 번호를 입력해주세요")
    String couponNumber;

    @Builder
    public CouponRequestDto(String couponNumber) {
        this.couponNumber = couponNumber;
    }
}