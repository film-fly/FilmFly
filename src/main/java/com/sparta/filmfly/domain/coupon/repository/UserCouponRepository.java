package com.sparta.filmfly.domain.coupon.repository;

import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.domain.coupon.entity.UserCoupon;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCouponRepository extends JpaRepository<UserCoupon, Long> {
    // UserId가 갖고있는 모든 쿠폰
    @Query("SELECT uc.coupon FROM UserCoupon uc WHERE uc.user.id = :userId")
    Page<Coupon> findAllCouponsByUserId(Long userId, Pageable pageable);
}
