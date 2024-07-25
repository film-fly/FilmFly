package com.sparta.filmfly.domain.coupon.controller;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.coupon.dto.CouponResponseDto;
import com.sparta.filmfly.domain.coupon.service.CouponService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/coupon")
public class CouponController {

    private final CouponService couponService;

    /**
     * Coupon 생성
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<List<CouponResponseDto>>> createCoupon(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CouponRequestDto requestDto
    ) {
        List<CouponResponseDto> responseDto = couponService.createCoupon(userDetails.getUser(),
                requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * Coupon 전체 조회
     */
    @GetMapping
    public ResponseEntity<DataResponseDto<List<CouponResponseDto>>> getAllCoupons() {
        List<CouponResponseDto> responseDtos = couponService.getAllCoupons();
        return ResponseUtils.success(responseDtos);
    }

    /**
     * Coupon 단일 조회
     */
    @GetMapping("/{couponId}")
    public ResponseEntity<DataResponseDto<CouponResponseDto>> getCoupon(
            @PathVariable Long couponId
    ) {
        CouponResponseDto responseDto = couponService.getCoupon(couponId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * Coupon 삭제
     */
    @DeleteMapping("/{couponId}")
    public ResponseEntity<DataResponseDto<CouponResponseDto>> deleteCoupon(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long couponId
    ) {
        CouponResponseDto responseDto = couponService.deleteCoupon(userDetails.getUser(), couponId);
        return ResponseUtils.success(responseDto);
    }

    /**
     * (이벤트) 유저에게 쿠폰 발급
     */
    @PostMapping("/event")
    public ResponseEntity<DataResponseDto<CouponResponseDto>> distributeCoupons(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ){
        CouponResponseDto responseDto = couponService.distributeCoupons(userDetails.getUser());
        return ResponseUtils.success(responseDto);
    }

}