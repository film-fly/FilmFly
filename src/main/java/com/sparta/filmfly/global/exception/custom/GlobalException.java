package com.sparta.filmfly.global.exception.custom;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import lombok.Getter;

@Getter
public class GlobalException extends RuntimeException {

    private final ResponseCodeEnum examCodeEnum;

    public GlobalException(ResponseCodeEnum examCodeEnum) {
        super(examCodeEnum.getMessage());
        this.examCodeEnum = examCodeEnum;
    }

}