package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class AccessDeniedException extends GlobalException {
    public AccessDeniedException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
