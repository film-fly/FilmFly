package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.EmailVerification;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmailVerificationToken(String token);

    Optional<EmailVerification> findByUser(User user);

    default EmailVerification findByEmailVerificationTokenOrElseThrow(String token) {
        return findByEmailVerificationToken(token)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.EMAIL_VERIFICATION_TOKEN_MISMATCH));
    }

    default EmailVerification findByUserOrElseThrow(User user) {
        return findByUser(user)
                .orElseThrow(() -> new NotFoundException(ResponseCodeEnum.USER_ALREADY_VERIFIED));
    }
}
