package com.sparta.filmfly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class EmailVerificationRequestDto {

    @NotBlank(message = "인증 코드를 입력해주세요.")
    private String code;
}
