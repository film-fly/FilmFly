package com.sparta.filmfly.domain.favorite.controller;

import com.sparta.filmfly.domain.favorite.entity.Favorite;
import com.sparta.filmfly.domain.favorite.service.FavoriteService;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/favorite")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    /**
    * 찜 등록하기
    */
    @PostMapping
    public ResponseEntity<MessageResponseDto> createFavorite(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestParam Long movieId
    ) {
        log.info("create favorite");
        favoriteService.createFavorite(userDetails.getUser(), movieId);
        return ResponseUtils.success();
    }

}