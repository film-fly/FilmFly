package com.sparta.filmfly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;


@Getter
public class UserLoginRequestDto {
    @NotBlank(message = "유저네임은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
