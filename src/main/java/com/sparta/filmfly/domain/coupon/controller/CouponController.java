package com.sparta.filmfly.domain.coupon.controller;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.coupon.dto.CouponResponseDto;
import com.sparta.filmfly.domain.coupon.service.CouponService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<DataResponseDto<CouponResponseDto>> createCoupon(
            @Valid @RequestBody CouponRequestDto requestDto
    ) {
        CouponResponseDto responseDto = couponService.createCoupon(requestDto);
        return ResponseUtils.success(responseDto);
    }

    @GetMapping
    public ResponseEntity<DataResponseDto<CouponResponseDto>> findCoupon(
            @PathVariable Long id
    ){
        CouponResponseDto responseDto = couponService.findCoupon(id);
        return ResponseUtils.success(responseDto);
    }

    @DeleteMapping
    public ResponseEntity<DataResponseDto<CouponResponseDto>> deleteCoupon(
            @PathVariable Long id
    ){
        CouponResponseDto responseDto = couponService.deleteCoupon(id);
        return ResponseUtils.success(responseDto);
    }

}