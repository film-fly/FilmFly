package com.sparta.filmfly.domain.comment.entity;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
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
@SQLRestriction("deleted_at IS NULL")
@SQLDelete(sql = "UPDATE comment SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends TimeStampEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @Column(nullable = false)
    private String content;

    private LocalDateTime deletedAt;

    @Builder // 필요한 것만 생성자로
    public Comment(User user,Board board, String content) {
        this.user = user;
        this.board = board;
        this.content = content;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }

    public CommentResponseDto toResponseDto() {
        return CommentResponseDto.builder()
            .content(this.content)
            .build();
    }

    /**
     * 요청한 유저가 해당 댓글 소유주인지 유효성 검사
     */
    public void checkBoardId(Long boardId) {
        if(this.board.getId() != boardId) {
            throw new AccessDeniedException(ResponseCodeEnum.COMMENT_PATH_MISMATCH);
        }
    }

    /**
     * 요청한 유저가 해당 댓글의 소유주인지 확인
     */
    public void checkOwnerUser(User requestUser) {
        if(!Objects.equals(this.user.getId(), requestUser.getId()))
            throw new AccessDeniedException(ResponseCodeEnum.COMMENT_NOT_OWNER);
    }
}