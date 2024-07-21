package com.sparta.filmfly.domain.user.dto;


import lombok.Getter;


@Getter
public class TokenResponseDto {
    private String accessToken;
    private String refreshToken;
}
