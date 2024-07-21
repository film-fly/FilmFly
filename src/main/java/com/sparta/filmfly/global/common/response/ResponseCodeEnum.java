package com.sparta.filmfly.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    REPORT_NOT_FOUND(HttpStatus.NOT_FOUND, "신고내용을 찾을 수 없습니다."),
    BLOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "차단내용을 찾을 수 없습니다."),
    REACTION_CONTENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "컨텐츠 타입을 찾을 수 없습니다."),
    GOOD_ALREADY_ADD(HttpStatus.CONFLICT, "이미 좋아요를 등록했습니다."),
    GOOD_ALREADY_REMOVE(HttpStatus.CONFLICT, "이미 좋아요를 취소했습니다."),



    ;
    private final HttpStatus httpStatus;
    private final String message;
}