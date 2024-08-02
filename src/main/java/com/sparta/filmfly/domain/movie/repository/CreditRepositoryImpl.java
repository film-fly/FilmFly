package com.sparta.filmfly.domain.movie.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.movie.entity.*;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class CreditRepositoryImpl implements CreditRepositoryCustom {
    private final JPAQueryFactory queryFactory;


    @Override
    public List<Credit> findByMovieId(Long movieId) {
        QMovie movie = QMovie.movie;
        QCredit credit = QCredit.credit;
        QMovieCredit movieCredit = QMovieCredit.movieCredit;

        return queryFactory.select(credit)
                .from(movieCredit)
                .join(movieCredit.movie, movie)
                .join(movieCredit.credit, credit)
                .where(movie.id.eq(movieId))
                .fetch();
    }
}
