package com.sparta.filmfly.domain.collection.controller;

import com.sparta.filmfly.domain.collection.dto.CollectionRequestDto;
import com.sparta.filmfly.domain.collection.dto.CollectionResponseDto;
import com.sparta.filmfly.domain.collection.service.CollectionService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
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
        CollectionResponseDto collectionResponseDtoList = collectionService.createCollection(userDetails.getUser(), collectionRequestDto);
        return ResponseUtils.success(collectionResponseDtoList);
    }
}