package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class LimitedException extends GlobalException {
    public LimitedException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
