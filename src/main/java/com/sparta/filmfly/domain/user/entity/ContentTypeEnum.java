package com.sparta.filmfly.domain.user.entity;

import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.collection.repository.CollectionRepository;
import com.sparta.filmfly.domain.comment.repository.CommentRepository;
import com.sparta.filmfly.domain.favorite.repository.FavoriteRepository;
import com.sparta.filmfly.domain.reaction.repository.BadRepository;
import com.sparta.filmfly.domain.reaction.repository.GoodRepository;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ContentTypeEnum {
    USER(UserRepository.class),
    BOARD(BoardRepository.class),
    BAD(BadRepository.class),
    GOOD(GoodRepository.class),
    COMMENT(CommentRepository.class),
    FAVORITE(FavoriteRepository.class),
    COLLECTION(CollectionRepository.class);

    private final Class<?> repositoryClass;

    public Class<?> getRepositoryClass() {
        return repositoryClass;
    }
}