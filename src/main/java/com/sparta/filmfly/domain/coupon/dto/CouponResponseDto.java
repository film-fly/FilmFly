package com.sparta.filmfly.domain.coupon.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.coupon.entity.Coupon;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CouponResponseDto {

    private Long id;

    @NotBlank
    String couponNumber;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    public static CouponResponseDto fromEntity(Coupon coupon) {
        return CouponResponseDto.builder()
                .id(coupon.getId())
                .couponNumber(coupon.getCouponNumber())
                .createdAt(LocalDateTime.now())
                .build();
    }

}