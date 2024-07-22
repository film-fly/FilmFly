package com.sparta.filmfly.domain.coupon.service;

import static com.sparta.filmfly.domain.user.entity.UserRoleEnum.ROLE_ADMIN;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.coupon.dto.CouponResponseDto;
import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.domain.coupon.repository.CouponRepository;
import com.sparta.filmfly.domain.user.entity.User;
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
    public CouponResponseDto createCoupon(User user,CouponRequestDto requestDto) {

        validUser(user);

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

    public CouponResponseDto deleteCoupon(User user, Long id) {
        validUser(user);
        Coupon coupon = couponRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.COUPON_NOT_FOUND)
        );

        couponRepository.delete(coupon);
        return CouponResponseDto.fromEntity(coupon);
    }

    public void validUser(User user) {
        if (user.getUserRole() != ROLE_ADMIN) {
            throw new UnAuthorizedException(ResponseCodeEnum.USER_UNAUTHORIZED);
        }
    }
}