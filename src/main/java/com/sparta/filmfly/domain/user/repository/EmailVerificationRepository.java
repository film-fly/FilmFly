package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.EmailVerification;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

    Optional<EmailVerification> findByEmail(String email);

    default EmailVerification findByEmailOrElseThrow(String email) {
        return findByEmail(email)
                .orElseThrow(() -> new DuplicateException(ResponseCodeEnum.EMAIL_NOT_FOUND));
    }

    default EmailVerification findByEmailOrCreateNew(String email) {
        return findByEmail(email)
                .orElseGet(() -> new EmailVerification(email));
    }
}
