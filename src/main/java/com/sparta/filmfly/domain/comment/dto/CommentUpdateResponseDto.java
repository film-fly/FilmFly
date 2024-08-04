package com.sparta.filmfly.domain.comment.dto;

import com.sparta.filmfly.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentUpdateResponseDto {
    private Long id;
    private String content;

    public static CommentUpdateResponseDto fromEntity(Comment comment) {
        return CommentUpdateResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .build();
    }
}
