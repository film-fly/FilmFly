package com.sparta.filmfly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
public class LoginRequestDto {
    @NotBlank(message = "유저네임은 필수 입력 값입니다.")
    private String username;

    @NotBlank(message = "비밀번호는 필수 입력 값입니다.")
    private String password;
}
