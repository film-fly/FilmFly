package com.sparta.filmfly.domain.coupon.repository;

import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
    default Coupon findByIdOrElseThrow(Long couponId) {
        return findById(couponId)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.COUPON_NOT_FOUND));
    }

    Boolean existsByTitle(String title);
    Optional<Coupon> findTopByStatusTrueOrderByCreatedAtAsc();
}