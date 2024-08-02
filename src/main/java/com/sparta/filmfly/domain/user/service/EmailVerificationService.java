package com.sparta.filmfly.domain.user.service;

import com.sparta.filmfly.domain.user.entity.EmailVerification;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.EmailVerificationRepository;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.ExpiredException;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class EmailVerificationService {

    private final UserRepository userRepository;
    private final EmailVerificationRepository verificationRepository;
    private final JavaMailSender mailSender;

    /**
     * 인증 코드를 생성하고 이메일로 전송
     */
    @Transactional
    public void createVerificationCode(String username) {
        User user = userRepository.findByUsernameOrElseThrow(username);

        // 유저 상태 검증
        user.validateDeletedStatus();
        user.validateSuspendedStatus();

        String token = generateVerificationToken();

        EmailVerification verification = EmailVerification.builder()
                .user(user)
                .emailVerificationToken(token)
                .emailExpiryTime(LocalDateTime.now().plusMinutes(3))
                .build();
        verificationRepository.save(verification);

        String recipientAddress = user.getEmail();
        String subject = "이메일 인증";
        String message = "이메일 인증번호는 다음과 같습니다: " + token;

        sendEmail(recipientAddress, subject, message);
    }

    /**
     * 인증 코드 재전송
     */
    @Transactional
    public void resendVerificationCode(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);

        // 유저 상태 검증
        user.validateDeletedStatus();
        user.validateSuspendedStatus();

        String token = generateVerificationToken();

        EmailVerification verification = verificationRepository.findByUserOrElseThrow(user);

        verification.validateResendLimit();
        verification.incrementResendCount();
        verification.updateLastResendTime(LocalDateTime.now());
        verification.updateEmailVerificationToken(token);
        verification.createEmailExpiryTime(LocalDateTime.now().plusMinutes(3));

        verificationRepository.save(verification);

        String recipientAddress = user.getEmail();
        String subject = "이메일 인증";
        String message = "이메일 인증번호는 다음과 같습니다: " + token;

        sendEmail(recipientAddress, subject, message);
    }

    /**
     * 사용자가 입력한 인증 코드를 검증하고 인증상태로 변경
     */
    @Transactional
    public void verifyEmail(String token) {
        EmailVerification verification = verificationRepository.findByEmailVerificationTokenOrElseThrow(token);

        if (verification.getEmailExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ExpiredException(ResponseCodeEnum.EMAIL_VERIFICATION_EXPIRED);
        }

        User user = verification.getUser();

        user.updateVerified();
        userRepository.save(user);

        verificationRepository.delete(verification);
    }

    /**
     * 이메일 전송
     */
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    /**
     * 6자리 인증 코드 생성
     */
    private String generateVerificationToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000);
        return String.valueOf(token);
    }
}

