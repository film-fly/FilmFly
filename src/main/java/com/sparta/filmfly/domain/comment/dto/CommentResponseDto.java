package com.sparta.filmfly.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.comment.entity.Comment;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentResponseDto {
    private String userName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    private long goodCount;
    private long badCount;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .userName(comment.getUser().getNickname())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .goodCount(comment.getGoodCount())
                .badCount(comment.getBadCount())
                .build();
    }

}