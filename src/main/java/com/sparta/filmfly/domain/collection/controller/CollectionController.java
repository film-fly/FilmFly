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
        List<CollectionResponseDto> collectionResponseDtoList= collectionService.getAllCollection(userDetails.getUser());
        return ResponseUtils.success(collectionResponseDtoList);
    }
}