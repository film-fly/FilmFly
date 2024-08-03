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


    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime expirationDate;

    Boolean issued;
    Boolean status;

    public static CouponResponseDto fromEntity(Coupon coupon) {
        return CouponResponseDto.builder()
                .id(coupon.getId())
                .title(coupon.getTitle())
                .description(coupon.getDescription())
                .issued(coupon.getIssued())
                .status(coupon.getStatus())
                .createdAt(LocalDateTime.now())
                .expirationDate(coupon.getExpirationDate())
                .build();
    }

}