package com.sparta.filmfly.domain.coupon.entity;

import static com.sparta.filmfly.domain.user.entity.UserRoleEnum.ROLE_ADMIN;

import com.sparta.filmfly.domain.coupon.dto.CouponRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.UnAuthorizedException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coupon extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String description;

    @Column(nullable = false)
    LocalDateTime expirationDate;

    // 발급 됐는지 안됐는지 확인하는 Column
    @Column(nullable = false)
    Boolean issued;

    @Column(nullable = false)
    Boolean status;

    @OneToMany(mappedBy = "coupon", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserCoupon> userCoupons = new HashSet<>();

    @Builder
    public Coupon(String title, CouponRequestDto requestDto) {
        this.title = title;
        this.description = requestDto.getDescription();
        this.expirationDate = requestDto.getExpirationDate();
        this.issued = false;
        this.status = true;
    }

    // 관리자 권한 검증하는 메서드
    public void validUser(User user) {
        if (user.getUserRole() != ROLE_ADMIN) {
            throw new UnAuthorizedException(ResponseCodeEnum.ACCESS_DENIED);
        }
    }

    public void updateIssuedTrue(){
        this.issued = true;
    }

    public void updateStatusFalse() {
        this.status = false;
    }
}