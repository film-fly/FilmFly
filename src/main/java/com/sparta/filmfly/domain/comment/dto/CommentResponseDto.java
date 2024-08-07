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
    private Long boardId;
    private String nickname;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private long goodCount;
    private long badCount;

    @Builder
    public CommentResponseDto(Long id, Long userId,Long boardId, String nickname, String content, LocalDateTime createdAt, long goodCount, long badCount) {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId;
        this.nickname = nickname;
        this.content = content;
        this.createdAt = createdAt;
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

    public static CommentResponseDto fromEntity(Comment comment,Long goodCount, Long badCount) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userId(comment.getUser().getId())
                .boardId(comment.getBoard().getId())
                .nickname(comment.getUser().getNickname())
                .content(comment.getContent())
                .createdAt(comment.getCreatedAt())
                .goodCount(goodCount)
                .badCount(badCount)
                .build();
    }
}