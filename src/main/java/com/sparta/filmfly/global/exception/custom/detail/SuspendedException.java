package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class SuspendedException extends GlobalException {
    public SuspendedException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
