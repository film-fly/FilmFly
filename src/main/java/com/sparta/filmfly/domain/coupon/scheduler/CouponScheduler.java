package com.sparta.filmfly.domain.coupon.scheduler;

import com.sparta.filmfly.domain.coupon.entity.Coupon;
import com.sparta.filmfly.domain.coupon.repository.CouponRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CouponScheduler {

    private CouponRepository couponRepository;

    // 매일 1시에 쿠폰 만료 점검
    @Scheduled(cron = "0 0 1 * * *")
    @Transactional
    public void updateCoupon() {
        LocalDateTime now = LocalDateTime.now();
        List<Coupon> list = couponRepository.findAllByStatusTrueOrderByExpirationDateAsc();

        for (Coupon coupon : list) {
            if (coupon.getCreatedAt().isAfter(now)) {
                break;
            }
            coupon.updateStatusFalse();
        }
    }
}
