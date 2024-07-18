package com.sparta.spring_init_template.global.exception.custom.exam;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ExamCodeEnum {
    EXAM_NOT_FOUND(HttpStatus.NOT_FOUND, "없다.");

    private final HttpStatus httpStatus;
    private final String message;
}