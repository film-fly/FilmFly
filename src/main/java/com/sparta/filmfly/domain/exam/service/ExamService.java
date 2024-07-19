package com.sparta.filmfly.domain.exam.service;

import com.sparta.filmfly.domain.exam.dto.ExamCreateRequestDto;
import com.sparta.filmfly.domain.exam.dto.ExamResponseDto;
import com.sparta.filmfly.domain.exam.dto.ExamUpdateRequestDto;
import com.sparta.filmfly.domain.exam.entity.Exam;
import com.sparta.filmfly.domain.exam.repository.ExamRepository;
import com.sparta.filmfly.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.filmfly.global.exception.custom.exam.detail.ExamDetailCustomException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ExamService {

    private final ExamRepository examRepository;

    // Exam 조회
    @Transactional(readOnly = true)
    public ExamResponseDto findById(Long examId) {
        Exam findExam = examRepository.findByIdOrElseThrow(examId);

        findExam.validateExam(); // ex) 회원 탈퇴 여부

        // 팩토리 메서드 or 빌더 사용
        return ExamResponseDto.fromEntity(findExam);
    }

    @Transactional(readOnly = true)
    public List<ExamResponseDto> findAll() {
        return examRepository.findAllExam();
    }

    // Exam 저장
    @Transactional
    public ExamResponseDto save(ExamCreateRequestDto requestDto) {
        Exam entity = requestDto.toEntity();

        Exam savedExam = examRepository.save(entity);

        return ExamResponseDto.fromEntity(savedExam);
    }

    // Exam 수정
    @Transactional
    public ExamResponseDto update(Long examId, ExamUpdateRequestDto requestDto) {
        Exam findExam = examRepository.findByIdOrElseThrow(examId);

        findExam.validateExam(); // ex) 회원 탈퇴 여부

        findExam.update(requestDto);

        return ExamResponseDto.fromEntity(findExam);
    }

    // Exam 삭제
    @Transactional
    public void delete(Long examId) {
        Exam findExam = examRepository.findByIdOrElseThrow(examId);

        findExam.validateExam(); // ex) 회원 탈퇴 여부

        examRepository.delete(findExam);
    }
}