package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReactionService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 해당 컨텐츠가 있는지 없는지 확인
     */
    public void checkContentExist(ReactionContentTypeEnum type, Long id) {
        if (type == ReactionContentTypeEnum.MOVIE) {
            movieRepository.findByIdOrElseThrow(id);
        } else if (type == ReactionContentTypeEnum.REVIEW) {
            reviewRepository.findByIdOrElseThrow(id);
        } else if (type == ReactionContentTypeEnum.BOARD) {
            boardRepository.findByIdOrElseThrow(id);
        } else if (type == ReactionContentTypeEnum.COMMENT) {
            commentRepository.findByIdOrElseThrow(id);
        }
    }
}