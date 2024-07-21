package com.sparta.filmfly.domain.user.controller;

import com.sparta.filmfly.domain.user.dto.EmailVerificationRequestDto;
import com.sparta.filmfly.domain.user.service.EmailVerificationService;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@RequiredArgsConstructor
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    // 이메일 인증 코드를 재전송
    @PostMapping("/{userId}/resend")
    public ResponseEntity<MessageResponseDto> resendVerificationCode(@PathVariable Long userId) {
        emailVerificationService.resendVerificationCode(userId);
        return ResponseUtils.success();
    }

    // 이메일 인증 코드를 검증
    @PostMapping("/verify")
    public ResponseEntity<MessageResponseDto> verifyCode(@RequestBody EmailVerificationRequestDto requestDto) {
        emailVerificationService.verifyEmail(requestDto.getCode());
        return ResponseUtils.success();
    }
}
