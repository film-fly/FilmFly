package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class NotOwnerException extends GlobalException {
    public NotOwnerException(ResponseCodeEnum responseCodeEnum) {
        super(responseCodeEnum);
    }
}
