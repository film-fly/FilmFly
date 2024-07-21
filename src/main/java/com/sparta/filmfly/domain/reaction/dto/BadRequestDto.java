package com.sparta.filmfly.domain.reaction.dto;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.Bad;
import com.sparta.filmfly.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BadRequestDto {

    @NotNull(message = "컨텐츠의 번호를 입력해 주세요.")
    private Long contentId;
    @NotBlank(message = "컨텐츠 타입을 입려해 주세요.")
    private String contentType;

    public Bad toEntity(User user, ReactionContentTypeEnum type) {
        return Bad.builder()
            .typeId(this.contentId)
            .type(type)
            .user(user)
            .build();
    }
}