package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.dto.GoodRequestDto;
import com.sparta.filmfly.domain.reaction.service.GoodService;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/good")
public class GoodController {

    private final GoodService goodService;
    private final UserRepository userRepository; // 로그인 기능 구현 시 제거

    @PostMapping
    public ResponseEntity<MessageResponseDto> addGood(
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        User loginUser = userRepository.findByIdOrElseThrow(1L); // 로그인 기능 구현 시 제거
        goodService.addGood(loginUser, requestDto);
        return ResponseUtils.success();
    }

    @DeleteMapping
    public ResponseEntity<MessageResponseDto> removeGood(
        @Valid @RequestBody GoodRequestDto requestDto
    ) {
        User loginUser = userRepository.findByIdOrElseThrow(1L); // 로그인 기능 구현 시 제거
        goodService.removeGood(loginUser, requestDto);
        return ResponseUtils.success();
    }
}