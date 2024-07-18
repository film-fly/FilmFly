package com.sparta.spring_init_template.domain.exam.service;

import com.sparta.spring_init_template.domain.exam.dto.ExamCreateRequestDto;
import com.sparta.spring_init_template.domain.exam.dto.ExamResponseDto;
import com.sparta.spring_init_template.domain.exam.dto.ExamUpdateRequestDto;
import com.sparta.spring_init_template.domain.exam.entity.Exam;
import com.sparta.spring_init_template.domain.exam.repository.ExamRepository;
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

        return ExamResponseDto.from(findExam);
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

        return ExamResponseDto.from(savedExam);
    }

    // Exam 수정
    @Transactional
    public ExamResponseDto update(Long examId, ExamUpdateRequestDto requestDto) {
        Exam findExam = examRepository.findByIdOrElseThrow(examId);

        findExam.validateExam(); // ex) 회원 탈퇴 여부

        findExam.update(requestDto);

        return ExamResponseDto.from(findExam);
    }

    // Exam 삭제
    @Transactional
    public void delete(Long examId) {
        Exam findExam = examRepository.findByIdOrElseThrow(examId);

        findExam.validateExam(); // ex) 회원 탈퇴 여부

        examRepository.delete(findExam);
    }
}