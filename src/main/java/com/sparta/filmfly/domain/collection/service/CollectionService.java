package com.sparta.filmfly.domain.collection.service;

import com.sparta.filmfly.domain.collection.dto.CollectionRequestDto;
import com.sparta.filmfly.domain.collection.dto.CollectionResponseDto;
import com.sparta.filmfly.domain.collection.dto.MovieCollectionRequestDto;
import com.sparta.filmfly.domain.collection.dto.MovieCollectionResponseDto;
import com.sparta.filmfly.domain.collection.entity.Collection;
import com.sparta.filmfly.domain.collection.entity.MovieCollection;
import com.sparta.filmfly.domain.collection.repository.CollectionRepository;
import com.sparta.filmfly.domain.collection.repository.MovieCollectionRepository;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CollectionService {
    private final CollectionRepository collectionRepository;
    private final MovieRepository movieRepository;
    private final MovieCollectionRepository movieCollectionRepository;

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
    * 보관함 내용 수정
    */
    @Transactional
    public CollectionResponseDto updateCollection(User user, Long collectionId, CollectionRequestDto collectionRequestDto) {
        // 보관함 존재 확인
        Collection collection = collectionRepository.findByIdOrElseThrow(collectionId);
        // 보관함 주인 확인
        collection.validateOwner(user);
        // 데이터 수정
        collection.update(collectionRequestDto.getName(), collectionRequestDto.getContent());
        return CollectionResponseDto.fromEntity(collection);
    }

    /**
    * 보관함 삭제
    */
    public void deleteCollection(User user, Long collectionId) {
        Collection collection = collectionRepository.findByIdOrElseThrow(collectionId);
        collection.validateOwner(user);
        collectionRepository.delete(collection);
    }

    /**
     * 보관함에 영화 등록
     */
    public void createMovieCollection(User user, MovieCollectionRequestDto movieCollectionRequestDto) {
        // 컬랙션 유무확인
        Collection collection = collectionRepository.findByIdOrElseThrow(movieCollectionRequestDto.getCollectionId());
        // 보관함 주인 확인
        collection.validateOwner(user);
        // 영화 유무확인
        Movie movie = movieRepository.findByIdOrElseThrow(movieCollectionRequestDto.getMovieId());
        // 영화 보관함 사전등록 여부 확인
        movieCollectionRepository.existsByCollection_idAndMovie_idOrElseThrow(movieCollectionRequestDto.getCollectionId(), movie.getId());
        // 객체 생성 및 저장
        MovieCollection movieCollection = MovieCollection.builder()
                .collection(collection)
                .movie(movie)
                .build();
        movieCollectionRepository.save(movieCollection);
    }

    public MovieCollectionResponseDto getMovieCollection(User user, Long collectionId) {
        // 보관함 여부 확인
        Collection collection = collectionRepository.findByIdOrElseThrow(collectionId);
        // 보관함 주인 확인
        collection.validateOwner(user);
        // 보관함에서 영화목록 조회
        List<MovieCollection> movieCollectionList = movieCollectionRepository.findByCollection_id(collection.getId());
        // 응답데이터 변환 및 반환
        return MovieCollectionResponseDto.fromEntity(collection, movieCollectionList.stream().map(
                MovieCollection::getMovie
        ).toList());
    }

    public void deleteMovieCollection(User user, Long collectionId, Long movieId) {
        // 보관함 존재 확인
        Collection collection = collectionRepository.findByIdOrElseThrow(collectionId);
        // 보관함 주인 확인
        collection.validateOwner(user);
        // 영화 존재 확인
        Movie movie = movieRepository.findByIdOrElseThrow(movieId);
        // 영화-보관함 존재 확인
        MovieCollection movieCollection = movieCollectionRepository.findByCollection_idAndMovie_idOrElseThrow(collection.getId(), movie.getId());
        // 보관함에서 영화 삭제
        movieCollectionRepository.delete(movieCollection);
    }
}