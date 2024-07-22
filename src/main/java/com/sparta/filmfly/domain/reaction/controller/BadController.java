package com.sparta.filmfly.domain.reaction.controller;

import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.service.BadService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/bad")
public class BadController {

    private final BadService badService;

    @PostMapping
    public ResponseEntity<MessageResponseDto> addBad(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody BadRequestDto requestDto
    ) {
        badService.addBad(userDetails.getUser(), requestDto);
        return ResponseUtils.success();
    }

    @DeleteMapping
    public ResponseEntity<MessageResponseDto> removeBad(
        @AuthenticationPrincipal UserDetailsImpl userDetails,
        @Valid @RequestBody BadRequestDto requestDto
    ) {
        badService.removeBad(userDetails.getUser(), requestDto);
        return ResponseUtils.success();
    }
}