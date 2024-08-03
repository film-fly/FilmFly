package com.sparta.filmfly.domain.favorite.controller;

import com.sparta.filmfly.domain.favorite.service.FavoriteService;
import com.sparta.filmfly.domain.movie.dto.MovieResponseDto;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
//@RequestMapping("")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
    * 찜 등록하기
    */
    @PostMapping("/{movieId}")
    public ResponseEntity<MessageResponseDto> createFavorite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId
    ) {
        log.info("create favorite");
        favoriteService.createFavorite(userDetails.getUser(), movieId);
        return ResponseUtils.success();
    }

    /**
    * 찜 조회하기
    */
    @GetMapping
    public ResponseEntity<DataResponseDto<List<MovieResponseDto>>> getFavorite(
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        log.info("get favorite");
        List<MovieResponseDto> movieResponseDtoList = favoriteService.getFavorite(userDetails.getUser());
        return ResponseUtils.success(movieResponseDtoList);
    }

    /**
    * 찜 취소하기
    */
    @DeleteMapping("/{movieId}")
    public ResponseEntity<MessageResponseDto> deleteFavorite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long movieId
    ) {
        log.info("delete favorite");
        favoriteService.deleteFavorite(userDetails.getUser(), movieId);
        return ResponseUtils.success();
    }
}