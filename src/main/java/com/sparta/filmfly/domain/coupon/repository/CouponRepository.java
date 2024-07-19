package com.sparta.filmfly.domain.coupon.repository;

import com.sparta.filmfly.domain.coupon.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {

}