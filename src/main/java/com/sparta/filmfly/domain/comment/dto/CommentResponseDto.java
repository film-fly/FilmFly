package com.sparta.filmfly.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.filmfly.domain.comment.entity.Comment;
import com.sparta.filmfly.domain.reaction.dto.ReactionCheckResponseDto;
import com.sparta.filmfly.domain.review.dto.ReviewReactionCheckResponseDto;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private Long userId;
    private Long boardId;
    private String nickname;
    private String profileImg;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime createdAt;
    private long goodCount;
    private long badCount;
    private Boolean isGood;
    private Boolean isBad;
    private Boolean isOwner;

    @Builder
    public CommentResponseDto(Long id, Long userId,Long boardId, String nickname, String profileImg, String content, LocalDateTime createdAt, long goodCount, long badCount, Boolean isGood, Boolean isBad) {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.content = content;
        this.createdAt = createdAt;
        this.goodCount = goodCount;
        this.badCount = badCount;
        this.isGood = false;
        this.isBad = false;
        this.isOwner = false;
    }

    public CommentResponseDto(Long id, Long userId, Long boardId, String nickname, String profileImg,
        String content, LocalDateTime createdAt, long goodCount, long badCount) {
        this.id = id;
        this.userId = userId;
        this.boardId = boardId;
        this.nickname = nickname;
        this.profileImg = profileImg;
        this.content = content;
        this.createdAt = createdAt;
        this.goodCount = goodCount;
        this.badCount = badCount;
        this.isOwner = false;
    }

    public static CommentResponseDto fromEntity(Comment comment,Long goodCount, Long badCount) {
        return CommentResponseDto.builder()
            .id(comment.getId())
            .userId(comment.getUser().getId())
            .boardId(comment.getBoard().getId())
            .nickname(comment.getUser().getNickname())
            .profileImg(comment.getUser().getPictureUrl())
            .content(comment.getContent())
            .createdAt(comment.getCreatedAt())
            .goodCount(goodCount)
            .badCount(badCount)
            .build();
    }

    public void setReactions(ReactionCheckResponseDto dto) {
        this.isGood = dto.getIsGood();
        this.isBad = dto.getIsBad();
    }

    public void setOwner(Boolean owner) {
        isOwner = owner;
    }
}