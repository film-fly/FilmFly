package com.sparta.filmfly.domain.board.entity;

import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AccessDeniedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Getter
@SQLRestriction("deleted_at IS NULL") // 조회 시 자동으로 삭제된 값은 빼고 가져옴
@SQLDelete(sql = "UPDATE board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private Long goodCount;

    @Column(nullable = false)
    private Long badCount;

    @Column(nullable = false)
    private Long hits;

    private LocalDateTime deletedAt;

    @Builder // 필요한 것만 생성자로
    public Board(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;

        goodCount = 0L;
        badCount = 0L;
        hits = 0L;
    }

    public void update(BoardRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : title;
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }

    public void addHits(){
        hits += 1;
    }

    /**
     * 요청한 유저가 해당 보드의 소유주인지 확인
     */
    public void validateOwner(User requestUser) {
        if(this.user.getId() != requestUser.getId())
           throw new AccessDeniedException(ResponseCodeEnum.BOARD_NOT_OWNER);
    }
}