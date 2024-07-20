package com.sparta.filmfly.domain.user.controller;

import com.sparta.filmfly.domain.user.dto.SignupRequestDto;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.service.UserService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<DataResponseDto<UserResponseDto>> signup(@RequestBody SignupRequestDto requestDto) {
        UserResponseDto responseDto = userService.signup(requestDto);
        return ResponseUtils.success(responseDto);
    }
}
