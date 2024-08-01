package com.sparta.filmfly.domain.board.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sparta.filmfly.domain.board.dto.BoardPageDto;
import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.entity.QBoard;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.entity.QBad;
import com.sparta.filmfly.domain.reaction.entity.QGood;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public BoardPageResponseDto findAllWithFilters(Pageable pageable, Long filterGoodCount, Long filterHits, String title) {
        QBoard board = QBoard.board;
        QGood good = QGood.good;
        QBad bad = QBad.bad;

        JPQLQuery<BoardPageDto> query = queryFactory
                .select(Projections.constructor(BoardPageDto.class,
                        board.id,
                        board.user.id,
                        board.title,
                        board.user.nickname,
                        board.createdAt,
                        good.id.count().as("goodCount"),
                        bad.id.count().as("badCount"),
                        board.hits
                ))
                .from(board)
                .leftJoin(good).on(good.type.eq(ReactionContentTypeEnum.BOARD).and(good.typeId.eq(board.id)))
                .leftJoin(bad).on(bad.type.eq(ReactionContentTypeEnum.BOARD).and(bad.typeId.eq(board.id)))
                .groupBy(board.id, board.user.id, board.title, board.user.nickname, board.createdAt, board.hits)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());

        if (filterGoodCount != null) {
            query.having(good.id.count().goe(filterGoodCount));
        }
        if (filterHits != null) {
            query.having(board.hits.goe(filterHits));
        }
        if (title != null && !title.isEmpty()) {
            query.where(board.title.containsIgnoreCase(title));
        }

        query.orderBy(board.createdAt.desc()); // 기본 정렬 조건: 생성 일자 최신순

        List<BoardPageDto> content = query.fetch();
        long total = query.fetchCount();  // 페이지의 총 요소 수를 가져옵니다.

        // PageImpl을 사용하여 페이지 정보를 생성합니다.
        PageImpl<BoardPageDto> page = new PageImpl<>(content, pageable, total);

        // BoardPageResponseDto를 반환합니다.
        return BoardPageResponseDto.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .currentPages(page.getNumber() + 1)
                .size(page.getSize())
                .content(content)
                .build();
    }

    public BoardPageResponseDto findAllByUserId(Long userId, Pageable pageable) {
        QBoard board = QBoard.board;
        QGood good = QGood.good;
        QBad bad = QBad.bad;

        JPQLQuery<BoardPageDto> query = queryFactory
                .select(Projections.constructor(BoardPageDto.class,
                        board.id,
                        board.user.id,
                        board.title,
                        board.user.nickname,
                        board.createdAt,
                        good.id.count().as("goodCount"),
                        bad.id.count().as("badCount"),
                        board.hits
                ))
                .from(board)
                .leftJoin(good).on(good.type.eq(ReactionContentTypeEnum.BOARD).and(good.typeId.eq(board.id)))
                .leftJoin(bad).on(bad.type.eq(ReactionContentTypeEnum.BOARD).and(bad.typeId.eq(board.id)))
                .where(board.user.id.eq(userId))
                .groupBy(board.id, board.user.id, board.title, board.user.nickname, board.createdAt, board.hits)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(board.createdAt.desc()); // 기본 정렬 조건: 생성 일자 최신순

        List<BoardPageDto> content = query.fetch();
        long total = query.fetchCount();  // 페이지의 총 요소 수를 가져옵니다.

        // PageImpl을 사용하여 페이지 정보를 생성합니다.
        PageImpl<BoardPageDto> page = new PageImpl<>(content, pageable, total);

        // BoardPageResponseDto를 반환합니다.
        return BoardPageResponseDto.builder()
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .currentPages(page.getNumber() + 1)
                .size(page.getSize())
                .content(content)
                .build();
    }
}
