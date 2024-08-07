package com.sparta.filmfly.domain.officeboard.entity;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotOwnerException;
import com.sparta.filmfly.global.exception.custom.detail.UnAuthorizedException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.util.Objects;

import static com.sparta.filmfly.domain.user.entity.UserRoleEnum.ROLE_ADMIN;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SQLDelete(sql = "UPDATE office_board SET deleted_at = CURRENT_TIMESTAMP where id = ?")
@SQLRestriction("deleted_at IS NULL")
public class OfficeBoard extends TimeStampEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    String title;

    @Column(nullable = false,columnDefinition = "TEXT")
    String content;

    @Column(nullable = false)
    Long hits;

    @Builder
    public OfficeBoard(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.hits = 0L;
    }

    public void addHits(){
        hits += 1;
    }

    public void updateTitleContent(String title, String content) {
        this.title = title != null ? title : this.title;
        this.content = content != null ? content : this.content;
    }

    public void deleteOfficeBoard() {
        setDeletedAt();
    }

    public void checkAdmin() {
        if (this.user.getUserRole() != ROLE_ADMIN) {
            throw new UnAuthorizedException(ResponseCodeEnum.ACCESS_DENIED);
        }
    }

    public void checkOwnerUser(User requestUser) {
        if(!Objects.equals(this.user.getId(),requestUser.getId())){
            throw new NotOwnerException(ResponseCodeEnum.BOARD_NOT_OWNER);
        }
    }
}