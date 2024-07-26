package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.dto.GoodRequestDto;
import com.sparta.filmfly.domain.reaction.entity.Good;
import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AlreadyActionException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class GoodService {

    private final ReactionService reactionService;
    private final GoodRepository goodRepository;

    /**
     * 좋아요 추가
     */
    @Transactional
    public void addGood(User loginUser, GoodRequestDto requestDto) {
        ReactionContentTypeEnum contentType = requestDto.getContentType();
        Long contentId = requestDto.getContentId();

        reactionService.checkContentExist(contentType,contentId);

        Good findGood = goodRepository.findByTypeIdAndTypeAndUser(
                contentId, contentType, loginUser
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
    @Transactional
    public void removeGood(User loginUser, GoodRequestDto requestDto) {
        ReactionContentTypeEnum contentType = requestDto.getContentType();
        Long contentId = requestDto.getContentId();

        reactionService.checkContentExist(contentType,contentId);

        // 좋아요가 없으면 예외
        Good findGood = goodRepository.findByTypeIdAndTypeAndUser(
                contentId, contentType, loginUser
        ).orElseThrow(() -> new NotFoundException(ResponseCodeEnum.GOOD_ALREADY_REMOVE));

        goodRepository.delete(findGood);
    }

    /**
     * 좋아요 카운트
     */
    public Long getCountByTypeTypeId(ReactionContentTypeEnum type, Long typeId) {
        return goodRepository.countByTypeAndTypeId(type,typeId);
    }

    /**
     * 좋아요한 유저인지 확인
     */
    public boolean checkGoodByUser(User loginUser, BadRequestDto requestDto) {
        Good findGood = goodRepository.findByTypeIdAndTypeAndUser(
                requestDto.getContentId(), requestDto.getContentType(), loginUser
        ).orElse(null);

        return findGood != null;
    }
}