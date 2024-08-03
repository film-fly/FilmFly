package com.sparta.filmfly.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private String username;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    private long goodCount;
    private long badCount;

    @Builder
    public CommentResponseDto(Long id, Long userId, String username, String content, LocalDateTime updatedAt, long goodCount, long badCount) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.content = content;
        this.updatedAt = updatedAt;
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

    public static CommentResponseDto fromEntity(Comment comment,Long goodCount, Long badCount) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .username(comment.getUser().getNickname())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .goodCount(goodCount)
                .badCount(badCount)
                .build();
    }
}