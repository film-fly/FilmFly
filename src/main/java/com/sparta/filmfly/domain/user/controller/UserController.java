package com.sparta.filmfly.domain.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.sparta.filmfly.domain.user.dto.*;
import com.sparta.filmfly.domain.user.service.KakaoService;
import com.sparta.filmfly.domain.user.service.UserService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<DataResponseDto<UserResponseDto>> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 카카오 로그인 요청
     */
    @GetMapping("/kakao/authorize")
    public void redirectToKakaoAuthorize(HttpServletResponse response) throws IOException {
        String requestUrl = String.format(
                "https://kauth.kakao.com/oauth/authorize?client_id=%s&redirect_uri=%s&response_type=code",
                clientId, redirectUri
        );
        response.sendRedirect(requestUrl);
    }

    /**
     * 카카오 콜백 처리
     */
    @GetMapping("/kakao/callback")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        UserResponseDto userResponseDto = kakaoService.kakaoLogin(code, response);
        if (userResponseDto != null) {
            return ResponseUtils.success(userResponseDto);
        } else {
            return ResponseUtils.success();
        }
    }

    /**
     * 비밀번호 변경
     */
    @PutMapping("/password")
    public ResponseEntity<MessageResponseDto> updatePassword(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserPasswordUpdateRequestDto requestDto
    ) {
        userService.updatePassword(userDetails.getUser(), requestDto);
        return ResponseUtils.success();
    }

    /**
     * 프로필 업로드
     */
    @PutMapping("/profile")
    public ResponseEntity<DataResponseDto<UserResponseDto>> updateProfile(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestPart("profileUpdateRequestDto") UserProfileUpdateRequestDto requestDto,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture
    ) {
        UserResponseDto responseDto = userService.updateProfile(userDetails.getUser(), requestDto, profilePicture);
        return ResponseUtils.success(responseDto);
    }

    /**
     * 닉네임 중복 확인
     */
    @PostMapping("/check-nickname")
    public ResponseEntity<MessageResponseDto> checkNicknameDuplication(@RequestBody UserNicknameCheckRequestDto requestDto) {
        userService.checkNicknameDuplication(requestDto.getNickname());
        return ResponseUtils.success();
    }

    /**
     * 프로필 조회
     */
    @GetMapping("/{userId}/profile")
    public ResponseEntity<DataResponseDto<UserResponseDto>> getProfile(@PathVariable Long userId) {
        UserResponseDto profile = userService.getProfile(userId);
        return ResponseUtils.success(profile);
    }

    /**
     * 로그아웃
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponseDto> logout(@AuthenticationPrincipal UserDetailsImpl userDetails, HttpServletResponse response) {
        userService.logout(userDetails.getUser());

        // 쿠키를 무효화하여 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();
        return ResponseUtils.success();
    }

    /**
     * 회원탈퇴
     */
    @DeleteMapping("/withdraw")
    public ResponseEntity<MessageResponseDto> deleteUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody UserDeleteRequestDto requestDto,
            HttpServletResponse response
    ) {
        log.info("deleteUser");
        userService.deleteUser(userDetails.getUser(), requestDto);

        // 쿠키를 무효화하여 삭제
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);

        SecurityContextHolder.clearContext();
        return ResponseUtils.success();
    }

    /**
     * 개인 유저 상세 조회 (관리자 기능)
     */
    @GetMapping("/search/detail")
    public ResponseEntity<DataResponseDto<UserResponseDto>> getUserDetail(
            @RequestBody UserSearchRequestDto userSearchRequestDto
    ) {
        UserResponseDto userDetail = userService.getUserDetail(userSearchRequestDto);
        return ResponseUtils.success(userDetail);
    }

    /**
     * 유저 상태별 조회 (관리자 기능)
     */
    @GetMapping("/search/status")
    public ResponseEntity<DataResponseDto<UserStatusSearchResponseDto>> getUsersByStatus(
            @RequestBody UserStatusSearchRequestDto userStatusRequestDto
    ) {
        UserStatusSearchResponseDto users = userService.getUsersByStatus(userStatusRequestDto.getStatus());
        return ResponseUtils.success(users);
    }

    /**
     * 유저 정지 (관리자 기능)
     */
    @PutMapping("/suspend/{userId}")
    public ResponseEntity<DataResponseDto<UserResponseDto>> suspendUser(
            @PathVariable Long userId
    ) {
        log.info("suspendUser");
        UserResponseDto userResponseDto = userService.suspendUser(userId);
        return ResponseUtils.success(userResponseDto);
    }

    /**
     * 유저 활성화 (관리자 기능)
     */
    @PutMapping("/activate/{userId}")
    public ResponseEntity<DataResponseDto<UserResponseDto>> activateUser(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long userId
    ) {
        log.info("activateUser");
        UserResponseDto userResponseDto = userService.activateUser(userId, userDetails.getUser());
        return ResponseUtils.success(userResponseDto);
    }
}
