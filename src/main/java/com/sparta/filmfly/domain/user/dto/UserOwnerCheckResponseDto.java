package com.sparta.filmfly.domain.user.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserOwnerCheckResponseDto {
    private String contentType;
    private Long contentId;
    private Boolean isOwner;
}