package com.sparta.filmfly.domain.coupon.service;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.coupon.dto.CouponResponseDto;
import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.domain.coupon.entity.UserCoupon;
import com.sparta.filmfly.domain.coupon.repository.CouponRepository;
import com.sparta.filmfly.domain.coupon.repository.UserCouponRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

    private final CouponRepository couponRepository;
    private final UserRepository userRepository;
    private final UserCouponRepository userCouponRepository;

    private static final int COUPON_CODE_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final Random random = new SecureRandom();

    /**
     * 쿠폰 발급
     */
    @Transactional
    public List<CouponResponseDto> createCoupon(User user, CouponRequestDto requestDto) {

        List<CouponResponseDto> list = new ArrayList<>();

        // 임의 객체 쿠폰 만든 후, 관리자 유저 검증
        Coupon tmpCoupon = new Coupon("1234", requestDto);
        tmpCoupon.validUser(user);

        String title = "";
        for (int i = 0; i < requestDto.getQuantity(); i++) {
            // 랜덤 값(title) 중복되면 반복문 돌아감
            do {
                title = generateRandomCouponCode();
            } while (couponRepository.existsByTitle(title));
            Coupon coupon = Coupon.builder()
                    .title(title)
                    .requestDto(requestDto)
                    .build();

            couponRepository.save(coupon);
            list.add(CouponResponseDto.fromEntity(coupon));
        }

        return list;
    }

    /**
     * 쿠폰 전체 조회
     */
    @Transactional(readOnly = true)
    public List<CouponResponseDto> getAllCoupons() {
        List<CouponResponseDto> list = couponRepository.findAll()
                .stream()
                .map(CouponResponseDto::fromEntity)
                .toList();

        return list;
    }

    /**
     * 쿠폰 조회
     */
    @Transactional(readOnly = true)
    public CouponResponseDto getCoupon(Long id) {
        Coupon coupon = couponRepository.findByIdOrElseThrow(id);
        return CouponResponseDto.fromEntity(coupon);
    }

    /**
     * 쿠폰 삭제
     */
    @Transactional
    public CouponResponseDto deleteCoupon(User user, Long id) {

        Coupon coupon = couponRepository.findByIdOrElseThrow(id);

        coupon.validUser(user);

        couponRepository.delete(coupon);
        return CouponResponseDto.fromEntity(coupon);
    }

    /**
     * 유저에게 쿠폰 발급
     */
    @Transactional
    public CouponResponseDto distributeCoupons(User user) {

        // 쿠폰 발급이 더이상 불가능하면 예외처리
        Coupon coupon = couponRepository.findTopByStatusTrueOrderByCreatedAtAsc().orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.COUPON_EXHAUSTED)
        );

        coupon.updateStatusFalse();

        UserCoupon userCoupon = UserCoupon.builder()
                .user(user)
                .coupon(coupon)
                .build();

        userCouponRepository.save(userCoupon);

        return CouponResponseDto.fromEntity(coupon);
    }

    /**
     * 쿠폰 번호 랜덤 배정하는 메서드
     */
    public String generateRandomCouponCode() {
        StringBuilder code = new StringBuilder(COUPON_CODE_LENGTH);
        for (int i = 0; i < COUPON_CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }

}