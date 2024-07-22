package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.entity.Bad;
import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import com.sparta.filmfly.domain.review.repository.ReviewRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyActionException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadService {

    private final BadRepository badRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    public void addBad(User loginUser, BadRequestDto requestDto) {
        ReactionContentTypeEnum contentType = ReactionContentTypeEnum.validateContentType(requestDto.getContentType());

        // 해당 컨텐츠가 있는지 없는지 확인
        checkContentExist(requestDto);

        Bad findBad = badRepository.findByTypeIdAndTypeAndUser(
            requestDto.getContentId(), contentType, loginUser
        ).orElse(null);

        // 이미 좋아요가 등록되어 있으면 예외
        if (null != findBad) {
            throw new AlreadyActionException(ResponseCodeEnum.BAD_ALREADY_ADD);
        }

        Bad bad = requestDto.toEntity(loginUser, contentType);
        badRepository.save(bad);
    }

    public void removeBad(User loginUser, BadRequestDto requestDto) {
        ReactionContentTypeEnum contentType = ReactionContentTypeEnum.validateContentType(requestDto.getContentType());

        // 해당 컨텐츠가 있는지 없는지 확인
        checkContentExist(requestDto);

        Bad findBad = badRepository.findByTypeIdAndTypeAndUser(
            requestDto.getContentId(), contentType, loginUser
        ).orElse(null);

        // 좋아요가 없으면 예외
        if (null == findBad) {
            throw new NotFoundException(ResponseCodeEnum.BAD_ALREADY_REMOVE);
        }

        badRepository.delete(findBad);
    }

    private void checkContentExist(BadRequestDto requestDto) {
        if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.MOVIE.getContentType())) {
            movieRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("영화가 없음"));  // 각 기능 다 되면 삭제
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.REVIEW.getContentType())) {
            reviewRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("리뷰가 없음"));  // 각 기능 다 되면 삭제
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.BOARD.getContentType())) {
            boardRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("게시물이 없음"));  // 각 기능 다 되면 삭제
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.COMMENT.getContentType())) {
            commentRepository.findById(requestDto.getContentId())
                .orElseThrow(() -> new IllegalArgumentException("댓글이 없음"));  // 각 기능 다 되면 삭제
        }
    }
}