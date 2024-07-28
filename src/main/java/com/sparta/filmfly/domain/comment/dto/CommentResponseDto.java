package com.sparta.filmfly.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.sparta.filmfly.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommentResponseDto {
    private Long id;
    private String userName;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime updatedAt;
    private Long goodCount;
    private Long badCount;

    public static CommentResponseDto fromEntity(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .userName(comment.getUser().getNickname())
                .content(comment.getContent())
                .updatedAt(comment.getUpdatedAt())
                .goodCount(0L)
                .badCount(0L)
                .build();
    }


    public void updateReactionCount(Long goodCount, Long badCount) {
        this.goodCount = goodCount;
        this.badCount = badCount;
    }

}