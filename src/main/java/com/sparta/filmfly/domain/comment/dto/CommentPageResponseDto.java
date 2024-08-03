package com.sparta.filmfly.domain.comment.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommentPageResponseDto {
    int totalPages;
    Long totalElements;
    int currentPage;
    int pageSize;
    List<CommentResponseDto> content;
}