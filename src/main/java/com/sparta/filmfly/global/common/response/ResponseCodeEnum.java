package com.sparta.filmfly.global.common.response;

import com.sparta.filmfly.domain.report.entity.Report;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ResponseCodeEnum {

    // Common
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),

    // Auth
    INVALID_REQUEST(HttpStatus.NOT_FOUND, "해당 요청을 처리 할 수 없습니다."),
    LOGIN_FAILED(HttpStatus.UNAUTHORIZED, "로그인 실패"),
    INVALID_TOKENS(HttpStatus.BAD_REQUEST, "유효하지 않는 토큰입니다."),

    // User
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    PASSWORD_INCORRECT(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다."),
    USER_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "사용자는 이미 인증되었습니다."),
    USER_DELETED(HttpStatus.BAD_REQUEST, "탈퇴한 사용자입니다."),
    USER_SUSPENDED(HttpStatus.FORBIDDEN, "정지된 사용자입니다."),
    USER_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 사용자입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "이미 존재하는 이메일입니다."),
    INVALID_ADMIN_PASSWORD(HttpStatus.FORBIDDEN, "관리자 비밀번호가 일치하지 않습니다."),
    INVALID_ADMIN_TARGET(HttpStatus.FORBIDDEN, "관리자를 대상으로 할 수 없습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    SAME_AS_OLD_PASSWORD(HttpStatus.BAD_REQUEST, "새 비밀번호가 현재 비밀번호와 동일합니다."),
    EMAIL_VERIFICATION_REQUIRED(HttpStatus.FORBIDDEN, "이메일 인증이 필요합니다."),
    EMAIL_VERIFICATION_EXPIRED(HttpStatus.BAD_REQUEST, "이메일 인증 코드가 만료되었습니다."),
    EMAIL_RESEND_LIMIT(HttpStatus.BAD_REQUEST, "한 시간내에 5회 까지만 요청이 가능합니다"),
    EMAIL_VERIFICATION_TOKEN_MISMATCH(HttpStatus.BAD_REQUEST, "인증번호가 일치하지 않습니다."),

    // Report
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고 처리 되었습니다."),

    //Block
    USER_ALREADY_BLOCKED(HttpStatus.BAD_REQUEST, "이미 차단된 사용자입니다."),
    NOT_BLOCKED_TARGET(HttpStatus.NOT_FOUND, "차단된 상대가 아닙니다."),


    ;
    private final HttpStatus httpStatus;
    private final String message;
}
