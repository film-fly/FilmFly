package com.sparta.filmfly.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentPageResponseDto {
    int totalPages;
    Long totalElements;
    int currentPages;
    int size;
    List<CommentResponseDto> content;
}
