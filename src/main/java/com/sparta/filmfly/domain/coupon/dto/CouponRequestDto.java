package com.sparta.filmfly.domain.coupon.dto;

import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AccessDeniedException;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
public class CouponRequestDto {

    @NotNull(message = "등록할 쿠폰의 총 개수를 입력해주세요.")
    private int quantity;

    //Title(쿠폰번호)는 service에서 랜덤값 배정하기에 Dto에 없습니다.

    @NotBlank(message = "쿠폰 설명을 입력해주세요.")
    private String description;

    @NotNull(message = "만료일을 입력해주세요.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    LocalDateTime expirationDate;

    @Builder
    public Coupon toEntity(String title, CouponRequestDto couponRequestDto) {
        return Coupon.builder()
                .title(title)
                .requestDto(couponRequestDto)
                .build();
    }
}