package com.sparta.filmfly.domain.comment.entity;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.comment.dto.CommentRequestDto;
import com.sparta.filmfly.domain.comment.dto.CommentResponseDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

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

    @Column(nullable = false)
    private Long goodCount;

    @Column(nullable = false)
    private Long badCount;

    private LocalDateTime deletedAt;

    @Builder // 필요한 것만 생성자로
    public Comment(User user,Board board, String content) {
        this.user = user;
        this.board = board;
        this.content = content;

        this.goodCount = 0L;
        this.badCount = 0L;
    }

    public void update(CommentRequestDto requestDto) {
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }

    public CommentResponseDto toResponseDto() {
        return CommentResponseDto.builder()
            .content(this.content)
            .build();
    }

    public void validateExam() {
        // exam 검증
        // ex) 사용자가 탈퇴 여부 검증
    }
}