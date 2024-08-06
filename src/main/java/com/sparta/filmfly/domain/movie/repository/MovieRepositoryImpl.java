package com.sparta.filmfly.domain.movie.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.movie.dto.MovieReactionsResponseDto;
import com.sparta.filmfly.domain.movie.dto.MovieSearchCond;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.entity.QMovie;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.QBad;
import com.sparta.filmfly.domain.reaction.entity.QGood;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MovieRepositoryImpl implements MovieRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<Movie> findByGenreIds(List<Integer> genreIds) {
        QMovie movie = QMovie.movie;

        BooleanExpression predicate = null;
        for (Integer genreId : genreIds) {
            BooleanExpression expression = movie.genreIds.any().eq(genreId);
            predicate = (predicate == null) ? expression : predicate.and(expression);
        }

        return queryFactory.selectFrom(movie)
                .where(predicate)
                .fetch();
    }

    @Override
    public PageResponseDto<List<MovieReactionsResponseDto>> getPageMovieBySearchCond(
        MovieSearchCond searchOptions,
        Pageable pageable
    ) {
        QMovie qMovie = QMovie.movie;
        QGood qGood = QGood.good;
        QBad qBad = QBad.bad;

        List<MovieReactionsResponseDto> fetch = queryFactory
            .select(Projections.constructor(MovieReactionsResponseDto.class,
                qMovie.id,
                qMovie.title,
                qMovie.originalTitle,
                qMovie.posterPath,
                qMovie.backdropPath,
                qGood.id.count().as("goodCount"),
                qBad.id.count().as("badCount")
            ))
            .from(qMovie)
            .leftJoin(qGood).on(qGood.type.eq(ReactionContentTypeEnum.MOVIE)
                .and(qGood.typeId.eq(qMovie.id)))
            .leftJoin(qBad).on(qBad.type.eq(ReactionContentTypeEnum.MOVIE)
                .and(qBad.typeId.eq(qMovie.id)))
            .where(searchPredicate(searchOptions))
            .groupBy(qMovie.id)
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        Long total = queryFactory
            .select(qMovie.count())
            .from(qMovie)
            .where(searchPredicate(searchOptions))
            .fetchOne();

        PageImpl<MovieReactionsResponseDto> page = new PageImpl<>(fetch, pageable, total);

        return PageResponseDto.<List<MovieReactionsResponseDto>>builder()
            .totalElements(page.getTotalElements())
            .totalPages(page.getTotalPages())
            .currentPage(page.getNumber() + 1)
            .pageSize(page.getSize())
            .data(fetch)
            .build();
    }

    private BooleanExpression searchPredicate(MovieSearchCond searchOptions) {
        QMovie qMovie = QMovie.movie;
        BooleanExpression predicate = qMovie.isNotNull();

        predicate = predicate.and(searchKeyword(searchOptions.getKeyword()));
        predicate = predicate.and(searchGenres(searchOptions.getGenreIds()));
        predicate = predicate.and(searchAdults(searchOptions.getAdults()));
        predicate = predicate.and(searchDateRange(searchOptions.getReleaseDateFrom(), searchOptions.getReleaseDateTo()));

        return predicate;
    }

    private BooleanExpression searchKeyword(String keyword) {
        QMovie qMovie = QMovie.movie;
        if (StringUtils.hasText(keyword)) {
            return qMovie.title.like("%" + keyword + "%");
        }
        return null;
    }

    private BooleanExpression searchGenres(List<Integer> genreIds) {
        QMovie qMovie = QMovie.movie;
        BooleanExpression predicate = null;
        if (!genreIds.isEmpty()) {
            for (Integer genreId : genreIds) {
                BooleanExpression expression = qMovie.genreIds.any().eq(genreId);
                // 현재는 genreIds에 포함되어 있는 모든 id값이 일치해야 출력을 함
                // 만약 하나라도 포함이 되어있는 영화를 출력하고 싶은면 or로 변경
                predicate = (predicate == null) ? expression : predicate.and(expression);
            }
        }
        return predicate;
    }

    private BooleanExpression searchAdults(List<Integer> adults) {
        QMovie qMovie = QMovie.movie;
        BooleanExpression predicate = null;
        if (!adults.isEmpty()) {
            for (Integer adult : adults) {
                BooleanExpression expression = qMovie.adult.eq(adult == 1);
                predicate = (predicate == null) ? expression : predicate.and(expression);
            }
        }
        return predicate;
    }

    private BooleanExpression searchDateRange(LocalDate releaseDateFrom, LocalDate releaseDateTo) {
        QMovie qMovie = QMovie.movie;
        BooleanExpression predicate = null;

        if (releaseDateFrom != null && releaseDateTo != null) {
            predicate = qMovie.releaseDate.between(releaseDateFrom.toString(), releaseDateTo.toString());
        } else if (releaseDateFrom != null) {
            predicate = qMovie.releaseDate.goe(releaseDateFrom.toString());
        } else if (releaseDateTo != null) {
            predicate = qMovie.releaseDate.loe(releaseDateTo.toString());
        }

        return predicate;
    }
}