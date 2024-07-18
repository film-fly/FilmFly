package com.sparta.spring_init_template.global.exception.custom.exam;

import lombok.Getter;

@Getter
public class ExamException extends RuntimeException {

    private final ExamCodeEnum examCodeEnum;

    public ExamException(ExamCodeEnum examCodeEnum) {
        super(examCodeEnum.getMessage());
        this.examCodeEnum = examCodeEnum;
    }

    // 사용 안 할 것 같긴 한데 일단 남겨둠
    public ExamException(ExamCodeEnum examCodeEnum, Throwable cause) {
        super(examCodeEnum.getMessage(), cause);
        this.examCodeEnum = examCodeEnum;
    }
}