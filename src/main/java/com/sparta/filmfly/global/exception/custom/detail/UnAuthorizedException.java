package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class UnAuthorizedException extends GlobalException {
    public UnAuthorizedException(ResponseCodeEnum responseCodeEnum){
        super(responseCodeEnum);
    }

}
