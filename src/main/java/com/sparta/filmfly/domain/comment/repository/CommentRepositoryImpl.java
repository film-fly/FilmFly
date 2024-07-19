package com.sparta.filmfly.domain.comment.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentQueryRepository {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<CommentResponseDto> findAllExam() {
        /*
        QExam qExam = QExam.exam;

        ConstructorExpression<ExamResponseDto> constructor = Projections.constructor(ExamResponseDto.class,
            qExam.title,
            qExam.content
        );

        return queryFactory.select(constructor)
            .from(qExam)
            .fetch();
        */
        return null;
    }
}