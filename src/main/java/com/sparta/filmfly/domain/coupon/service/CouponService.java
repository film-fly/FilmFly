package com.sparta.filmfly.domain.coupon.service;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.coupon.dto.CouponResponseDto;
import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.domain.coupon.repository.CouponRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;

    @Transactional
    public CouponResponseDto createCoupon(CouponRequestDto requestDto) {
        Coupon coupon = Coupon.builder()
                .requestDto(requestDto)
                .build();

        couponRepository.save(coupon);
        return CouponResponseDto.fromEntity(coupon);
    }

    @Transactional
    public CouponResponseDto findCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.COUPON_NOT_FOUND)
        );

        return CouponResponseDto.fromEntity(coupon);
    }

    public CouponResponseDto deleteCoupon(Long id) {
        Coupon coupon = couponRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.COUPON_NOT_FOUND)
        );

        couponRepository.delete(coupon);
        return CouponResponseDto.fromEntity(coupon);
    }
}