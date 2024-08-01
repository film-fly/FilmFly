package com.sparta.filmfly.domain.user.controller;

import com.sparta.filmfly.domain.report.dto.ReportResponseDto;
import com.sparta.filmfly.domain.report.service.ReportService;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.dto.UserSearchRequestDto;
import com.sparta.filmfly.domain.user.dto.UserStatusSearchRequestDto;
import com.sparta.filmfly.domain.user.dto.UserStatusSearchResponseDto;
import com.sparta.filmfly.domain.user.service.UserService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ReportService reportService;

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
    @PatchMapping("/suspend/{userId}")
    public ResponseEntity<DataResponseDto<UserResponseDto>> suspendUser(
            @PathVariable Long userId
    ) {
        UserResponseDto userResponseDto = userService.suspendUser(userId);
        return ResponseUtils.success(userResponseDto);
    }

    /**
     * 신고 목록 조회 (관리자 권한)
     */
    @GetMapping("/report")
    public ResponseEntity<DataResponseDto<List<ReportResponseDto>>> getAllReports() {
        List<ReportResponseDto> reports = reportService.getAllReports();
        return ResponseUtils.success(reports);
    }
}