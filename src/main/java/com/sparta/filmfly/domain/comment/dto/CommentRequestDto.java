package com.sparta.filmfly.domain.comment.dto;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {
    @NotBlank(message = "내용 입력하세요.")
    private String content;

    public Comment toEntity(User user, Board board) {
        return Comment.builder()
                .user(user)
                .board(board)
                .content(content)
                .build();
    }
}