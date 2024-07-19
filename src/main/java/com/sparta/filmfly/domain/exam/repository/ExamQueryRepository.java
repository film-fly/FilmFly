package com.sparta.filmfly.domain.exam.repository;

import com.sparta.filmfly.domain.exam.dto.ExamResponseDto;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public interface ExamQueryRepository {

    List<ExamResponseDto> findAllExam();
}