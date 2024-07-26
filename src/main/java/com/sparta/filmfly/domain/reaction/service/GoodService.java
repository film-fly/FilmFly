package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.GoodRequestDto;
import com.sparta.filmfly.domain.reaction.entity.Good;
import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
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
public class GoodService {

    private final GoodRepository goodRepository;
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    /**
     * 좋아요 추가
     */
    public void addGood(User loginUser, GoodRequestDto requestDto) {
        ReactionContentTypeEnum contentType = ReactionContentTypeEnum.validateContentType(requestDto.getContentType());

        // 해당 컨텐츠가 있는지 없는지 확인
        checkContentExist(requestDto);

        Good findGood = goodRepository.findByTypeIdAndTypeAndUser(
            requestDto.getContentId(), contentType, loginUser
        ).orElse(null);

        // 이미 좋아요가 등록되어 있으면 예외
        if (findGood != null) {
            throw new AlreadyActionException(ResponseCodeEnum.GOOD_ALREADY_ADD);
        }

        Good good = requestDto.toEntity(loginUser, contentType);
        goodRepository.save(good);
    }

    /**
     * 좋아요 취소
     */
    public void removeGood(User loginUser, GoodRequestDto requestDto) {
        ReactionContentTypeEnum contentType = ReactionContentTypeEnum.validateContentType(requestDto.getContentType());

        // 해당 컨텐츠가 있는지 없는지 확인
        checkContentExist(requestDto);

        // 좋아요가 없으면 예외
        Good findGood = goodRepository.findByTypeIdAndTypeAndUser(
            requestDto.getContentId(), contentType, loginUser
        ).orElseThrow(() -> new NotFoundException(ResponseCodeEnum.GOOD_ALREADY_REMOVE));

        goodRepository.delete(findGood);
    }

    /**
     * 좋아요 카운트
     */
    public Long getCountByTypeTypeId(ReactionContentTypeEnum type, Long typeId) {
        return goodRepository.countByTypeAndTypeId(type,typeId);
    }

    private void checkContentExist(GoodRequestDto requestDto) {
        Long id = requestDto.getContentId();
        if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.MOVIE.getContentType())) {
            movieRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("영화가 없음"));  // 각 기능 다 되면 삭제
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.REVIEW.getContentType())) {
            reviewRepository.findByIdOrElseThrow(id);
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.BOARD.getContentType())) {
            boardRepository.findByIdOrElseThrow(id);
        } else if (requestDto.getContentType().equalsIgnoreCase(ReactionContentTypeEnum.COMMENT.getContentType())) {
            commentRepository.findByIdOrElseThrow(id);
        }
    }
}