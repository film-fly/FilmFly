package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class DuplicateException extends GlobalException {
    public DuplicateException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
