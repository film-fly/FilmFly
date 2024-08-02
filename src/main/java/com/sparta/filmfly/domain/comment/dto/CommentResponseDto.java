package com.sparta.filmfly.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long commentId;
    private Long userId;
    private String username;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    private long goodCount;
    private long badCount;

    public static CommentResponseDto fromEntity(Comment comment,Long goodCount, Long badCount) {
        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getNickname())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .goodCount(goodCount)
                .badCount(badCount)
                .build();
    }

}