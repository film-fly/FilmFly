package com.sparta.filmfly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class UserDeleteRequestDto {
    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}