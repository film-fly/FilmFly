package com.sparta.filmfly.global.util;

import java.security.SecureRandom;
import java.util.Random;

public class StringUtils {

    private static final int COUPON_CODE_LENGTH = 8;
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final Random random = new SecureRandom();

    /**
     * 쿠폰 번호 랜덤 배정하는 메서드
     */
    public static String generateRandomCouponCode() {
        StringBuilder code = new StringBuilder(COUPON_CODE_LENGTH);
        for (int i = 0; i < COUPON_CODE_LENGTH; i++) {
            code.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }
        return code.toString();
    }
}
