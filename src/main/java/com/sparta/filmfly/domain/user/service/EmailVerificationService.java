package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.user.entity.EmailVerification;
import com.sparta.filmfly.domain.user.repository.EmailVerificationRepository;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.DuplicateException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final EmailVerificationRepository emailVerificationRepository;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;

    @Transactional
    public void sendVerificationEmail(String email) {
        // 사용자 테이블에서 이메일 중복 확인
        if (userRepository.existsByEmail(email)) {
            throw new DuplicateException(ResponseCodeEnum.EMAIL_ALREADY_EXISTS);
        }

        // 이메일 인증 엔티티 조회, 존재하지 않으면 새로 생성
        EmailVerification emailVerification = emailVerificationRepository.findByEmailOrCreateNew(email);

        // 재전송 제한 체크
        emailVerification.validateSendLimit();

        // 새로운 인증 코드 생성 및 갱신
        emailVerification.updateEmailVerificationToken();
        emailVerification.incrementSendCount();
        emailVerification.updateLastResendTime(LocalDateTime.now());
        emailVerificationRepository.save(emailVerification);

        // 이메일 발송
        sendEmail(email, "이메일 인증 코드는 다음과 같습니다: " + emailVerification.getEmailVerificationToken());
    }

    private void sendEmail(String to, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("이메일 인증 코드");
        message.setText(text);

        mailSender.send(message);
    }

    @Transactional
    public void verifyEmailCode(String email, String code) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmailOrElseThrow(email);
        emailVerification.validateToken(code);
        emailVerification.validateExpiryTime();
        emailVerification.verify();
        emailVerificationRepository.save(emailVerification);
    }

    @Transactional
    public void checkIfEmailVerified(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmailOrElseThrow(email);
        emailVerification.validateVerifiedStatus();
    }

    @Transactional
    public void deleteEmailVerificationByEmail(String email) {
        EmailVerification emailVerification = emailVerificationRepository.findByEmailOrElseThrow(email);
        emailVerificationRepository.delete(emailVerification);
    }
}


