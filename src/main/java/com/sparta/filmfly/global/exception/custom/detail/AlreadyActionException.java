package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class AlreadyActionException extends GlobalException {
    public AlreadyActionException(ResponseCodeEnum examCodeEnum) {
        super(examCodeEnum);
    }
}