package com.sparta.filmfly.domain.comment.dto;

import com.sparta.filmfly.domain.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    @NotBlank
    private String userName;
    @NotBlank
    private String content;
    @NotNull
    private LocalDateTime updatedAt;
    @NotNull
    private long goodCount;

    public CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
            .userName(comment.getUser().getNickname())
            .content(comment.getContent())
            .updatedAt(comment.getUpdatedAt())
            .goodCount(comment.getGoodCount())
            .build();
    }

}