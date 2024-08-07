package com.sparta.filmfly.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserNicknameCheckRequestDto {

    @Size(min = 3, max = 12, message = "닉네임은 3~12 글자만 가능합니다.")
    private String nickname;
}