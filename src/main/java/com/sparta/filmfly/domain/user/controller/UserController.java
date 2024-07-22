package com.sparta.filmfly.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.filmfly.domain.user.dto.PasswordUpdateRequestDto;
import com.sparta.filmfly.domain.user.dto.ProfileUpdateRequestDto;
import com.sparta.filmfly.domain.user.dto.SignupRequestDto;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.service.KakaoService;
import com.sparta.filmfly.domain.user.service.UserService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;

    @Value("${CLIENT_ID}")
    private String clientId;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<DataResponseDto<UserResponseDto>> signup(@RequestBody SignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseUtils.success(responseDto);
    }

    // 카카오 로그인 요청
    @GetMapping("/kakao/authorize")
    public void redirectToKakaoAuthorize(HttpServletResponse response) throws IOException {
        String requestUrl = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                clientId, redirectUri
        );
        response.sendRedirect(requestUrl);
    }

    // 카카오 콜백 처리 첫 로그인시 데이터 생성이 필요하므로 생성한 데이터 응답, 다음 로그인시는 데이터없이 응답
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        UserResponseDto userResponseDto = kakaoService.kakaoLogin(code, response);
        if (userResponseDto != null) {
            return ResponseUtils.success(userResponseDto);
        } else {
            return ResponseUtils.success();
        }
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<MessageResponseDto> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl loginUser,
            @Validated @RequestBody PasswordUpdateRequestDto requestDto
    ) {
        userService.updatePassword(loginUser.getUser(), requestDto);
        return ResponseUtils.success();
    }

    // 프로필 업로드
    @PutMapping("/profile")
    public ResponseEntity<MessageResponseDto> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Validated @RequestPart("profileUpdateRequestDto") ProfileUpdateRequestDto requestDto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        userService.updateProfile(userDetails.getUser(), requestDto, profilePicture);
        return ResponseUtils.success();
    }

    // 프로필 조회
    @GetMapping("/{userId}/profile")
    public ResponseEntity<DataResponseDto<UserResponseDto>> getProfile(@PathVariable Long userId) {
        UserResponseDto profile = userService.getProfile(userId);
        return ResponseUtils.success(profile);
    }
}
