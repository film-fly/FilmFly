package com.sparta.filmfly.domain.board.entity;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.AccessDeniedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(nullable = false,columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private Long hits;

    private LocalDateTime deletedAt;

    @Builder // 필요한 것만 생성자로
    public Board(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;

        hits = 0L;
    }

    public void updateContent(String title, String content) {
        this.title = title != null ? title : this.title;
        this.content = content != null ? content : this.content;
    }

    public void addHits(){
        hits += 1;
    }

    public void updateHits(Long hits) {
        this.hits = hits;
    }

    /**
     * 요청한 유저가 해당 보드의 소유주인지 확인
     */
    public void checkOwnerUser(User requestUser) {
        if(!Objects.equals(this.user.getId(), requestUser.getId()))
           throw new AccessDeniedException(ResponseCodeEnum.BOARD_NOT_OWNER);
    }
}