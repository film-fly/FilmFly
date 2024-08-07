package com.sparta.filmfly.domain.reaction.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.movie.entity.QMovie;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.ReactionMovieResponseDto;
import com.sparta.filmfly.domain.reaction.entity.QGood;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class GoodRepositoryImpl implements GoodRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    /**
     * 사용자가 좋아요를 누른 영화 조회 (페이징)
     */
    @Override
    public PageResponseDto<List<ReactionMovieResponseDto>> getPageMovieByUserGood(
        Long userId, Pageable pageable
    ) {
        QMovie qMovie = QMovie.movie;
        QGood qGood = QGood.good;

        List<ReactionMovieResponseDto> fetch = queryFactory
            .select(Projections.constructor(ReactionMovieResponseDto.class,
                qGood.id,
                qMovie.id.as("movieId"),
                qMovie.title,
                qMovie.originalTitle,
                qMovie.posterPath,
                qMovie.backdropPath
            ))
            .from(qGood)
            .join(qMovie).on(qGood.typeId.eq(qMovie.id)
                .and(qGood.user.id.eq(userId))
            )
            .where(qGood.type.eq(ReactionContentTypeEnum.MOVIE))
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(qMovie.count())
            .from(qMovie)
            .innerJoin(qGood).on(qGood.type.eq(ReactionContentTypeEnum.MOVIE)
                .and(qGood.typeId.eq(qMovie.id))
                .and(qGood.user.id.eq(userId)))
            .fetchOne();

        PageImpl<ReactionMovieResponseDto> page = new PageImpl<>(fetch, pageable, total);

        return PageResponseDto.<List<ReactionMovieResponseDto>>builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber() + 1)
            .pageSize(page.getSize())
            .data(page.getContent())
            .build();
    }
}