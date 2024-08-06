package com.sparta.filmfly.domain.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.QBad;
import com.sparta.filmfly.domain.reaction.entity.QGood;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewUserResponseDto;
import com.sparta.filmfly.domain.review.entity.QReview;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ReviewRepositoryImpl implements ReviewRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public PageResponseDto<List<ReviewResponseDto>> getPageReviewByMovieId(Long movieId, Pageable pageable) {
        QReview qReview = QReview.review;
        QGood qGood = QGood.good;
        QBad qBad = QBad.bad;

        List<ReviewResponseDto> fetch = queryFactory.select(Projections.constructor(
            ReviewResponseDto.class,
                qReview.id,
                qReview.user.id,
                qReview.user.nickname,
                qReview.user.pictureUrl,
                qReview.rating,
                qReview.title,
                qReview.content,
                qReview.createdAt,
                qGood.id.count().as("goodCount"),
                qBad.id.count().as("badCount")
            ))
            .from(qReview)
            .leftJoin(qGood).on(qGood.type.eq(ReactionContentTypeEnum.REVIEW)
                .and(qGood.typeId.eq(qReview.id)))
            .leftJoin(qBad).on(qBad.type.eq(ReactionContentTypeEnum.REVIEW)
                .and(qBad.typeId.eq(qReview.id)))
            .where(qReview.movie.id.eq(movieId))
            .groupBy(qReview.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(qReview.createdAt.desc())
            .fetch();

        Long total = queryFactory
            .select(qReview.count())
            .from(qReview)
            .where(qReview.movie.id.eq(movieId))
            .fetchOne();

        PageImpl<ReviewResponseDto> page = new PageImpl<>(fetch, pageable, total);

        return PageResponseDto.<List<ReviewResponseDto>>builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber() + 1)
            .pageSize(page.getSize())
            .data(fetch)
            .build();
    }

    /**
     * 유저의 리뷰 목록
     */
    @Override
    public PageResponseDto<List<ReviewUserResponseDto>> getPageReviewByUserId(Long userId, Pageable pageable) {
        QReview qReview = QReview.review;
        QGood qGood = QGood.good;
        QBad qBad = QBad.bad;

        List<ReviewUserResponseDto> fetch = queryFactory.select(Projections.constructor(
                        ReviewUserResponseDto.class,
                        qReview.id,
                        qReview.user.id,
                        qReview.movie.id,
                        qReview.movie.title,
                        qReview.user.nickname,
                        qReview.user.pictureUrl,
                        qReview.rating,
                        qReview.title,
                        qReview.content,
                        qReview.createdAt,
                        qGood.id.count().as("goodCount"),
                        qBad.id.count().as("badCount")
                ))
                .from(qReview)
                .leftJoin(qGood).on(qGood.type.eq(ReactionContentTypeEnum.REVIEW)
                        .and(qGood.typeId.eq(qReview.id)))
                .leftJoin(qBad).on(qBad.type.eq(ReactionContentTypeEnum.REVIEW)
                        .and(qBad.typeId.eq(qReview.id)))
                .where(qReview.user.id.eq(userId))
                .groupBy(qReview.id)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(qReview.createdAt.desc())
                .fetch();

        Long total = queryFactory
                .select(qReview.count())
                .from(qReview)
                .where(qReview.user.id.eq(userId))
                .fetchOne();

        PageImpl<ReviewUserResponseDto> page = new PageImpl<>(fetch, pageable, total);

        return PageResponseDto.<List<ReviewUserResponseDto>>builder()
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .currentPage(page.getNumber() + 1)
                .pageSize(page.getSize())
                .data(fetch)
                .build();
    }

    @Override
    public Float getAverageRatingByMovieId(Long movieId) {
        QReview qReview = QReview.review;

        Double averageRating = queryFactory
            .select(qReview.rating.avg())
            .from(qReview)
            .where(qReview.movie.id.eq(movieId)
                .and(qReview.deletedAt.isNull())) // 조건을 추가하여 삭제된 리뷰를 제외
            .fetchOne();

        if (averageRating == null) {
            averageRating = 0.0;
        }

        return Math.round(averageRating * 10) / 10.0f;
    }
}