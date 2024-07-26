package com.sparta.filmfly.domain.collection.service;

import com.sparta.filmfly.domain.collection.dto.CollectionRequestDto;
import com.sparta.filmfly.domain.collection.dto.CollectionResponseDto;
import com.sparta.filmfly.domain.collection.entity.Collection;
import com.sparta.filmfly.domain.collection.repository.CollectionRepository;
import com.sparta.filmfly.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;

    /**
    * 보관함 생성
    */
    public CollectionResponseDto createCollection(User user, CollectionRequestDto collectionRequestDto) {
        // 이미 있는 보관함인지 확인
        collectionRepository.existsByUser_IdAndNameOrElseThrow(user.getId(), collectionRequestDto.getName());

        // 보관함 생성, 저장
        Collection collection = Collection.builder()
                .user(user)
                .name(collectionRequestDto.getName())
                .content(collectionRequestDto.getContent())
                .build();
        collection = collectionRepository.save(collection);
        return CollectionResponseDto.fromEntity(collection);
    }

    /**
    * 보관함 목록 조회
    */
    public List<CollectionResponseDto> getAllCollection(User user) {
        // 자신이 생성한 보관함들 조회
        List<Collection> collectionList = collectionRepository.findAllByUser(user);
        return collectionList.stream().map(
                CollectionResponseDto::fromEntity
        ).toList();
    }

    /**
    * 보관함 삭제
    */
    public void deleteCollection(User user, Long collectionId) {
        Collection collection = collectionRepository.findByIdOrElseThrow(collectionId);
        collection.validateOwner(user);
        collectionRepository.delete(collection);
    }
}