package com.sparta.filmfly.domain.officeboard.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficeBoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    String content;

    @Builder
    public OfficeBoardRequestDto(String title, String content) {
        this.title = title;
        this.content = content;
    }

}