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
    UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드에 실패했습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),
    INVALID_ADMIN_TARGET(HttpStatus.FORBIDDEN, "관리자를 대상으로 할 수 없습니다."),
    USER_ALREADY_BLOCKED(HttpStatus.BAD_REQUEST, "이미 차단된 사용자입니다."),
    NOT_BLOCKED_TARGET(HttpStatus.NOT_FOUND, "차단된 상대가 아닙니다."),
    ALREADY_REPORTED(HttpStatus.BAD_REQUEST, "이미 신고 처리 되었습니다."),

    // Favorite
    FAVORITE_ALREADY_EXISTS(HttpStatus.ALREADY_REPORTED, "찜이 이미 등록되어있습니다."),

    // Movie
    MOVIE_NOT_FOUND(HttpStatus.NOT_FOUND, "영화를 찾을 수 없습니다."),

    //Board
    BOARD_NOT_FOUND(HttpStatus.NOT_FOUND, "보드를 찾을 수 없습니다."),
    BOARD_NOT_OWNER(HttpStatus.FORBIDDEN, "보드의 주인이 아닙니다."),
    //Comment
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_NOT_OWNER(HttpStatus.FORBIDDEN, "댓글의 주인이 아닙니다."),
    // Review
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "리뷰를 찾을 수 없습니다."),
    REVIEW_NOT_OWNER(HttpStatus.FORBIDDEN, "본인이 작성한 리뷰가 아닙니다."),
    //REACTION
    REACTION_CONTENT_TYPE_NOT_FOUND(HttpStatus.NOT_FOUND, "컨텐츠 타입을 찾을 수 없습니다."),
    GOOD_ALREADY_ADD(HttpStatus.CONFLICT, "이미 좋아요를 등록했습니다."),
    GOOD_ALREADY_REMOVE(HttpStatus.CONFLICT, "이미 좋아요를 취소했습니다."),
    BAD_ALREADY_ADD(HttpStatus.CONFLICT, "이미 싫어요를 등록했습니다."),
    BAD_ALREADY_REMOVE(HttpStatus.CONFLICT, "이미 싫어요를 취소했습니다."),

    // 호진 (Coupon)
    COUPON_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 쿠폰을 찾을 수 없습니다"),
    COUPON_EXHAUSTED(HttpStatus.NOT_FOUND, "이벤트 쿠폰이 모두 소진되었습니다."),
    ;
    private final HttpStatus httpStatus;
    private final String message;
}
