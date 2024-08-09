package com.sparta.filmfly.domain.board.dto;

import com.sparta.filmfly.domain.reaction.dto.ReactionCheckResponseDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoardReactionResponseDto {
    BoardResponseDto board;
    ReactionCheckResponseDto reactions;

    public static BoardReactionResponseDto of(BoardResponseDto board, ReactionCheckResponseDto reactions) {
        return BoardReactionResponseDto.builder()
            .board(board)
            .reactions(reactions)
            .build();
    }
}