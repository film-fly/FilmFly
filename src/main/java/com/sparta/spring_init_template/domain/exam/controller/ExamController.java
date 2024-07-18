package com.sparta.spring_init_template.domain.exam.controller;

import com.sparta.spring_init_template.global.common.response.DataResponseDto;
import com.sparta.spring_init_template.global.common.response.MessageResponseDto;
import com.sparta.spring_init_template.global.common.response.ResponseUtils;
import com.sparta.spring_init_template.domain.exam.dto.ExamCreateRequestDto;
import com.sparta.spring_init_template.domain.exam.dto.ExamResponseDto;
import com.sparta.spring_init_template.domain.exam.dto.ExamUpdateRequestDto;
import com.sparta.spring_init_template.domain.exam.service.ExamService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/exam")
public class ExamController {
    private final ExamService examService;

    // Exam 생성
    @PostMapping
    public ResponseEntity<DataResponseDto<ExamResponseDto>> save(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody ExamCreateRequestDto requestDto
    ) {
        ExamResponseDto responseDto = examService.save(requestDto);
        return ResponseUtils.success(responseDto);
    }

    // Exam 조회
    @GetMapping("/{examId}")
    public ResponseEntity<DataResponseDto<ExamResponseDto>> findById(
            @PathVariable Long examId
    ) {
        ExamResponseDto responseDto = examService.findById(examId);
        return ResponseUtils.success(responseDto);
    }

    // Exam 조회
    @GetMapping
    public ResponseEntity<DataResponseDto<List<ExamResponseDto>>> findAll() {
        List<ExamResponseDto> responseDto = examService.findAll();
        return ResponseUtils.success(responseDto);
    }

    // Exam 수정
    @PutMapping("/{examId}")
    public ResponseEntity<DataResponseDto<ExamResponseDto>> update(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long examId,
            @Valid @RequestBody ExamUpdateRequestDto requestDto
    ) {
        ExamResponseDto responseDto = examService.update(examId, requestDto);
        return ResponseUtils.success(responseDto);
    }

    // Exam 삭제
    @DeleteMapping("/{examId}")
    public ResponseEntity<MessageResponseDto> delete(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long examId
    ) {
        examService.delete(examId);
        return ResponseUtils.success();
    }
}