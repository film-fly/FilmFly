package com.sparta.filmfly.domain.reaction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReactionContentTypeEnum {
    MOVIE("movie"),
    REVIEW("review"),
    BOARD("board"),
    COMMENT("comment");

    private final String contentType;

    @JsonCreator
    public static ReactionContentTypeEnum validateContentType(String contentType) {
        for (ReactionContentTypeEnum reactionContentTypeEnum : ReactionContentTypeEnum.values()) {
            if (reactionContentTypeEnum.getContentType().equalsIgnoreCase(contentType)) {
                return reactionContentTypeEnum;
            }
        }
        throw new NotFoundException(ResponseCodeEnum.REACTION_CONTENT_TYPE_NOT_FOUND);
    }
    @JsonValue
    public String getContentType() {
        return contentType;
    }
}