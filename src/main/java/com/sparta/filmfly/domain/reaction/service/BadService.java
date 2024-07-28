package com.sparta.filmfly.domain.reaction.service;

import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.BadRequestDto;
import com.sparta.filmfly.domain.reaction.entity.Bad;
import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import com.sparta.filmfly.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class BadService {

    private final ReactionService reactionService;
    private final BadRepository badRepository;

    /**
     * 싫어요 추가
     */
    @Transactional
    public void addBad(User loginUser, BadRequestDto requestDto) {
        ReactionContentTypeEnum contentType = requestDto.getContentType();
        Long contentId = requestDto.getContentId();

        reactionService.checkContentExist(contentType,contentId);

        // 이미 싫어요가 등록되어 있으면 예외
        badRepository.existsByTypeIdAndTypeAndUserIdOrElseThrow(
                contentId, contentType, loginUser.getId()
        );

        Bad bad = requestDto.toEntity(loginUser, contentType);
        badRepository.save(bad);
    }

    /**
     * 싫어요 취소
     */
    @Transactional
    public void removeBad(User loginUser, BadRequestDto requestDto) {
        ReactionContentTypeEnum contentType = requestDto.getContentType();
        Long contentId = requestDto.getContentId();

        reactionService.checkContentExist(contentType,contentId);

        // 싫어요가 없으면 예외
        Bad findBad = badRepository.findByTypeIdAndTypeAndUserIdOrElseThrow(
                contentId, contentType, loginUser.getId()
        );

        badRepository.delete(findBad);
    }

    /**
     * 싫어요 카운트
     */
    public Long getCountByTypeTypeId(ReactionContentTypeEnum type, Long typeId) {
        return badRepository.countByTypeAndTypeId(type,typeId);
    }

    /**
     * 싫어요한 유저인지 확인
     */
    public boolean checkBadByUser(User loginUser, BadRequestDto requestDto) {
        return badRepository.existsByTypeIdAndTypeAndUserId(
                requestDto.getContentId(), requestDto.getContentType(), loginUser.getId()
        );
    }
}