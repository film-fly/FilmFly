package com.sparta.filmfly.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.coupon.entity.Coupon;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponseDto {

    private Long id;
    private String title;
    private String description;
    private double discountRate;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    Boolean status;

    public static CouponResponseDto fromEntity(Coupon coupon) {
        return CouponResponseDto.builder()
                .id(coupon.getId())
                .title(coupon.getTitle())
                .description(coupon.getDescription())
                .discountRate(coupon.getDiscountRate())
                .status(coupon.getStatus())
                .createdAt(LocalDateTime.now())
                .build();
    }

}