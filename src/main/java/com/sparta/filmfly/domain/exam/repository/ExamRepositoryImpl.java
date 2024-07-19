package com.sparta.filmfly.domain.exam.repository;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.exam.dto.ExamResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ExamRepositoryImpl implements ExamQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<ExamResponseDto> findAllExam() {
        QExam qExam = QExam.exam;

        ConstructorExpression<ExamResponseDto> constructor = Projections.constructor(ExamResponseDto.class,
            qExam.title,
            qExam.content
        );

        return queryFactory.select(constructor)
            .from(qExam)
            .fetch();
    }
}