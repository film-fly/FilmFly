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

    // 인증 코드를 생성하고 이메일로 전송하는 메서드
    @Transactional
    public void createVerificationCode(String username) {
        User user = userRepository.findByUsernameOrElseThrow(username);
        String token = generateVerificationToken();

        // 새로운 인증 코드 생성
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

    // 기존 인증 코드를 재전송하는 메서드
    @Transactional
    public void resendVerificationCode(Long userId) {
        User user = userRepository.findByIdOrElseThrow(userId);
        String token = generateVerificationToken();

        // EmailVerification 객체를 조회하고 없으면 예외 발생
        EmailVerification verification = verificationRepository.findByUserOrElseThrow(user);

        // 재전송 가능 여부 검증
        verification.validateResendLimit();

        // 재전송 횟수 증가 및 마지막 재전송 시간 업데이트
        verification.incrementResendCount();
        verification.updateLastResendTime(LocalDateTime.now());

        // 기존 인증 코드 업데이트
        verification.updateEmailVerificationToken(token);
        verification.createEmailExpiryTime(LocalDateTime.now().plusMinutes(3)); // 3분간 유효

        verificationRepository.save(verification);

        String recipientAddress = user.getEmail();
        String subject = "이메일 인증";
        String message = "이메일 인증번호는 다음과 같습니다: " + token;

        sendEmail(recipientAddress, subject, message);
    }

    // 사용자가 입력한 인증 코드를 검증하고 인증상태로 변경 하는 메서드
    @Transactional
    public void verifyEmail(String token) {
        // 인증 코드로 EmailVerification 객체를 조회합니다. 없으면 예외를 발생시킵니다.
        EmailVerification verification = verificationRepository.findByEmailVerificationTokenOrElseThrow(token);

        // 인증 코드의 유효 기간을 확인합니다. 만료되었으면 예외를 발생시킵니다.
        if (verification.getEmailExpiryTime().isBefore(LocalDateTime.now())) {
            throw new ExpiredException(ResponseCodeEnum.EMAIL_VERIFICATION_EXPIRED);
        }

        // 인증이 성공하면 유저의 상태를 VERIFIED로 변경하고 저장
        User user = verification.getUser();
        user.updateVerified(); // 상태 업데이트 메서드 호출
        userRepository.save(user);

        // 사용된 인증 코드는 삭제
        verificationRepository.delete(verification);
    }

    // 이메일을 전송하는 메서드
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);

        mailSender.send(message);
    }

    // 6자리 인증 코드를 생성하는 메서드
    private String generateVerificationToken() {
        Random random = new Random();
        int token = 100000 + random.nextInt(900000); // 6자리 숫자 생성
        return String.valueOf(token);
    }

}
