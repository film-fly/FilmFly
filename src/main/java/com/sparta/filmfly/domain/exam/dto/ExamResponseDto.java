package com.sparta.filmfly.domain.exam.dto;

import com.sparta.filmfly.domain.exam.entity.Exam;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ExamResponseDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public static ExamResponseDto fromEntity(Exam exam) {
        return ExamResponseDto.builder()
            .title(exam.getTitle())
            .content(exam.getContent())
            .build();
    }
}