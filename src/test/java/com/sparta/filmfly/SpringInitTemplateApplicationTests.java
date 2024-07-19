package com.sparta.filmfly;

import com.sparta.filmfly.domain.exam.dto.ExamCreateRequestDto;
import com.sparta.filmfly.domain.exam.dto.ExamResponseDto;
import com.sparta.filmfly.domain.exam.entity.Exam;
import com.sparta.filmfly.domain.exam.repository.ExamRepository;
import com.sparta.filmfly.domain.exam.service.ExamService;
import javax.crypto.ExemptionMechanismException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

@SpringBootTest
class SpringInitTemplateApplicationTests {

    private final ExamService examService;
    private final ExamRepository examRepository;

    @Test
    void contextLoads() {
        Exam savedExam = examRepository.save(1L);
        ExamCreateRequestDto dto = new ExamCreateRequestDto();
        ReflectionTestUtils.setField(dto, "id", 1L);
    }

}