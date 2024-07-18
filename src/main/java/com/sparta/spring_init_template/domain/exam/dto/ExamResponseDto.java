package com.sparta.spring_init_template.domain.exam.dto;

import com.sparta.spring_init_template.domain.exam.entity.Exam;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamResponseDto {
    private String title;
    private String content;

    public static ExamResponseDto from(Exam exam) {
        return ExamResponseDto.builder()
                .title(exam.getTitle())
                .content(exam.getContent())
                .build();
    }
}