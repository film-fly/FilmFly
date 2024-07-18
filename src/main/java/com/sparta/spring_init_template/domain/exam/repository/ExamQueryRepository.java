package com.sparta.spring_init_template.domain.exam.repository;

import com.sparta.spring_init_template.domain.exam.dto.ExamResponseDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQueryRepository {

    List<ExamResponseDto> findAllExam();
}