package com.sparta.filmfly.domain.coupon.entity;

import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.global.common.TimeStampEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_board_id", nullable = false)
    private OfficeBoard officeBoard;

    @Column(nullable = false)
    String couponNumber;

    @Column(nullable = false)
    Boolean status;

    @Builder
    public Coupon(String couponNumber){
        this.couponNumber = couponNumber;
    }
}