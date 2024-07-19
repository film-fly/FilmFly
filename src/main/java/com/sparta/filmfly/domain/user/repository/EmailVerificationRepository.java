package com.sparta.filmfly.domain.user.repository;

import com.sparta.filmfly.domain.user.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailVerificationRepository extends JpaRepository<EmailVerification, Long> {

}