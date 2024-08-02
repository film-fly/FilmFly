package com.sparta.filmfly.domain.movie.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.entity.QMovie;
import lombok.RequiredArgsConstructor;

import java.util.List;

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

}
