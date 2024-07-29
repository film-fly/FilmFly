package com.sparta.filmfly.domain.collection.controller;

import com.sparta.filmfly.domain.collection.dto.*;
import com.sparta.filmfly.domain.collection.service.CollectionService;
import com.sparta.filmfly.domain.movie.dto.MovieResponseDto;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collection")
@RequiredArgsConstructor
public class CollectionController {

    private final CollectionService collectionService;

    /**
     * 보관함 생성
     */
    @PostMapping
    public ResponseEntity<DataResponseDto<CollectionResponseDto>> createCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @Valid @RequestBody CollectionRequestDto collectionRequestDto
    ) {
        CollectionResponseDto collectionResponseDto = collectionService.createCollection(userDetails.getUser(), collectionRequestDto);
        return ResponseUtils.success(collectionResponseDto);
    }

    /**
     * 보관함 목록 조회
     */
    @GetMapping
    public ResponseEntity<DataResponseDto<List<CollectionResponseDto>>> getAllCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        List<CollectionResponseDto> collectionResponseDtoList = collectionService.getAllCollection(userDetails.getUser());
        return ResponseUtils.success(collectionResponseDtoList);
    }

    /**
     * 보관함 상세 조회 _ 영화 목록 조회
     */
    @GetMapping("/{collectionId}/detail")
    public ResponseEntity<DataResponseDto<MovieCollectionResponseDto>> getMovieCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long collectionId
    ) {
        MovieCollectionResponseDto movieCollectionResponseDto = collectionService.getMovieCollection(userDetails.getUser(), collectionId);
        return ResponseUtils.success(movieCollectionResponseDto);
    }

    /**
     * 보관함 수정
     */
    @PatchMapping("/{collectionId}")
    public ResponseEntity<DataResponseDto<CollectionResponseDto>> updateCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long collectionId,
            @Valid @RequestBody CollectionRequestDto collectionRequestDto
    ) {
        CollectionResponseDto collectionResponseDto = collectionService.updateCollection(userDetails.getUser(), collectionId, collectionRequestDto);
        return ResponseUtils.success(collectionResponseDto);
    }


    /**
     * 보관함 삭제
     */
    @DeleteMapping("/{collectionId}")
    public ResponseEntity<MessageResponseDto> deleteCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long collectionId
    ) {
        collectionService.deleteCollection(userDetails.getUser(), collectionId);
        return ResponseUtils.success();
    }

    /**
     * 보관함에 영화 등록
     */
    @PostMapping("/{collectionId}/addMovie/{movieId}")
    public ResponseEntity<MessageResponseDto> createMovieCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long collectionId,
            @PathVariable Long movieId
    ) {
        collectionService.createMovieCollection(userDetails.getUser(), collectionId, movieId);
        return ResponseUtils.success();
    }

    /**
     * 보관함에서 영화 삭제
     */
    @DeleteMapping("/{collectionId}/deleteMovie/{movieId}")
    public ResponseEntity<MessageResponseDto> deleteMovieCollection(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long collectionId,
            @PathVariable Long movieId
    ) {
        collectionService.deleteMovieCollection(userDetails.getUser(), collectionId, movieId);
        return ResponseUtils.success();
    }
}