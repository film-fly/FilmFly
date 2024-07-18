package com.sparta.spring_init_template.global.exception.custom.exam.detail;

import com.sparta.spring_init_template.global.exception.custom.exam.ExamCodeEnum;
import com.sparta.spring_init_template.global.exception.custom.exam.ExamException;

public class ExamDetailCustomException extends ExamException {

    public ExamDetailCustomException(ExamCodeEnum examCodeEnum) {
        super(examCodeEnum);
    }
}