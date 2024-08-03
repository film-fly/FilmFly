package com.sparta.filmfly.domain.user.controller;

import com.sparta.filmfly.domain.report.dto.ReportResponseDto;
import com.sparta.filmfly.domain.report.service.ReportService;
import com.sparta.filmfly.domain.user.dto.UserResponseDto;
import com.sparta.filmfly.domain.user.dto.UserSearchResponseDto;
import com.sparta.filmfly.domain.user.dto.UserStateChangeRequestDto;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.domain.user.service.UserService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;
    private final ReportService reportService;

    /**
     * 유저 검색 조회
     */
    @GetMapping("/users")
    public ResponseEntity<DataResponseDto<UserSearchResponseDto>> getUsersBySearch(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) UserStatusEnum status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        UserSearchResponseDto users = userService.getUsersBySearch(search, status, page, size);
        return ResponseUtils.success(users);
    }

    /**
     * 개인 유저 상세 조회
     */
    @GetMapping("/users/{userId}")
    public ResponseEntity<DataResponseDto<UserResponseDto>> getUserDetail(
            @PathVariable Long userId
    ) {
        UserResponseDto userDetail = userService.getUserDetail(userId);
        return ResponseUtils.success(userDetail);
    }

    /**
     * 신고 목록 조회
     */
    @GetMapping("/reports")
    public ResponseEntity<DataResponseDto<List<ReportResponseDto>>> getAllReports() {
        List<ReportResponseDto> reports = reportService.getAllReports();
        return ResponseUtils.success(reports);
    }

    /**
     * 유저 정지
     */
    @PatchMapping("/suspend")
    public ResponseEntity<DataResponseDto<UserResponseDto>> suspendUser(
            @RequestBody UserStateChangeRequestDto userStateChangeRequestDto
    ) {
        UserResponseDto userResponseDto = userService.suspendUser(userStateChangeRequestDto.getUserId());
        return ResponseUtils.success(userResponseDto);
    }


    /**
     * 유저 활성화 시키기
     */
    @PatchMapping("/activate")
    public ResponseEntity<DataResponseDto<UserResponseDto>> activateUser(
            @RequestBody UserStateChangeRequestDto userStateChangeRequestDto
    ) {
        UserResponseDto userResponseDto = userService.activateUserAsAdmin(userStateChangeRequestDto.getUserId());
        return ResponseUtils.success(userResponseDto);
    }


}