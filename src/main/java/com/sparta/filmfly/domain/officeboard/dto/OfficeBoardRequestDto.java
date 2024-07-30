package com.sparta.filmfly.domain.officeboard.dto;

import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OfficeBoardRequestDto {

    @NotBlank(message = "제목을 입력해주세요.")
    String title;

    @NotBlank(message = "내용을 입력해주세요.")
    String content;


    public OfficeBoard toEntity(User user, OfficeBoardRequestDto requestDto) {

        return OfficeBoard.builder()
                .user(user)
                .requestDto(requestDto)
                .build();
    }

}