package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class NotFoundException extends GlobalException {
    public NotFoundException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}