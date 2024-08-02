package com.sparta.filmfly.global.exception.custom.detail;

import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.GlobalException;

public class UploadException extends GlobalException {
    public UploadException(ResponseCodeEnum responseCodeEnum) { super(responseCodeEnum);}
}
