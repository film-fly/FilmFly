package com.sparta.filmfly.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    EMAIL_VERIFICATION_REQUIRED(HttpStatus.FORBIDDEN, "이메일 인증이 필요합니다."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),
    USER_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 사용자입니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    INVALID_TOKENS(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰입니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    USER_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "사용자는 이미 인증되었습니다."),
    INVALID_ADMIN_PASSWORD(HttpStatus.FORBIDDEN, "관리자 비밀번호가 일치하지 않습니다."),
    EMAIL_VERIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 만료되었습니다."),
    EMAIL_VERIFICATION_TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호가 현재 비밀번호와 동일합니다."),

    // Movie
    API_REQUEST_FAILED(HttpStatus.BAD_REQUEST, "API 요청에 실패했습니다."),

    // Credit
    CREDIT_NOT_FOUND(HttpStatus.NOT_FOUND, "배우를 찾을 수 없습니다.")
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
