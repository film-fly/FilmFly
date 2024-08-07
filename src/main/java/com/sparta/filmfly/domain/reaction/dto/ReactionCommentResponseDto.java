package com.sparta.filmfly.domain.reaction.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ReactionCommentResponseDto {
    private Long id;
    private Long commentId;
    private Long userId;
    private Long boardId;
    private String nickname;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
}