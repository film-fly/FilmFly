package com.sparta.filmfly.domain.officeboard.entity;

import static com.sparta.filmfly.domain.user.entity.UserRoleEnum.ROLE_ADMIN;

import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.global.common.TimeStampEntity;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotOwnerException;
import com.sparta.filmfly.global.exception.custom.detail.UnAuthorizedException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.util.List;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.web.multipart.MultipartFile;

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

    @Column(nullable = false)
    String content;

    @Column(nullable = false)
    Long hits;

    @Column(nullable = false)
    Long goodCount;


    @Builder
    public OfficeBoard(User user, String title, String content) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.hits = 0L;
        this.goodCount = 0L;
    }

    public void updateOfficeBoard(OfficeBoardRequestDto requestDto) {
        this.title = requestDto.getTitle() != null ? requestDto.getTitle() : title;
        this.content = requestDto.getContent() != null ? requestDto.getContent() : content;
    }

    public void deleteOfficeBoard() {
        setDeletedAt();
    }

    public void validUser() {
        if (this.user.getUserRole() != ROLE_ADMIN) {
            throw new UnAuthorizedException(ResponseCodeEnum.USER_UNAUTHORIZED);
        }
    }

    public void checkOwnerUser(User requestUser) {
        if(!Objects.equals(this.user.getId(),requestUser.getId())){
            throw new NotOwnerException(ResponseCodeEnum.OFFICEBOARD_NOT_OWNER);
        }
    }

    public boolean isFilesNotNull(List<MultipartFile> files){
        if (files == null || files.isEmpty() || files.get(0).isEmpty()) {
            return false;
        }
        return true;
    }
}