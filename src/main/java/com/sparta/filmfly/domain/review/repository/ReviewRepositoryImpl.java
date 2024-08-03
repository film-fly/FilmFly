package com.sparta.filmfly.domain.review.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.movie.dto.PageResponseDto;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.QBad;
import com.sparta.filmfly.domain.reaction.entity.QGood;
import com.sparta.filmfly.domain.review.dto.ReviewResponseDto;
import com.sparta.filmfly.domain.review.entity.QReview;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

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

    @Override
    public Float getAverageRatingByMovieId(Long movieId) {
        QReview qReview = QReview.review;

        Double averageRating = queryFactory
            .select(qReview.rating.avg())
            .from(qReview)
            .where(qReview.movie.id.eq(movieId)
                .and(qReview.deletedAt.isNull())) // 조건을 추가하여 삭제된 리뷰를 제외
            .fetchOne();

        return Math.round(averageRating * 10) / 10.0f;
    }
}