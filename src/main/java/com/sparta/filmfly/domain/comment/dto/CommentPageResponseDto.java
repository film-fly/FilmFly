package com.sparta.filmfly.domain.comment.dto;

import com.sparta.filmfly.domain.comment.entity.Comment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Builder
public class CommentPageResponseDto {
    int totalPages;
    Long totalElements;
    int currentPages;
    int size;
    List<CommentResponseDto> content;

    public static CommentPageResponseDto fromPage(Page<Comment> comment) {
        List<CommentResponseDto> commentsDto = comment.stream().map(CommentResponseDto::fromEntity).toList();

        return CommentPageResponseDto.builder()
                .totalPages(comment.getTotalPages())
                .totalElements(comment.getTotalElements())
                .currentPages(comment.getNumber()+1)
                .size(comment.getSize())
                .content(commentsDto)
                .build();
    }
}
