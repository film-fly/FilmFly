package com.sparta.filmfly.domain.movie.controller;

import com.sparta.filmfly.domain.movie.dto.ApiMovieRequestDto;
import com.sparta.filmfly.domain.movie.dto.ApiMovieResponseDto;
import com.sparta.filmfly.domain.movie.dto.MovieGetRequestDto;
import com.sparta.filmfly.domain.movie.dto.MovieGetResponseDto;
import com.sparta.filmfly.domain.movie.service.MovieService;
import com.sparta.filmfly.global.common.response.DataResponseDto;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/movie")
@RequiredArgsConstructor
public class MovieController {
    private final MovieService movieService;

    /**
    * API 데이터 크롤링
    */
    @PostMapping("/api")
    public ResponseEntity<DataResponseDto<List<ApiMovieResponseDto>>> apiDiscoverMovieRequest(
            @RequestBody ApiMovieRequestDto apiMovieRequestDto
    ) {
        // 영화 검색
        log.info("In searchMovieList");
        List<ApiMovieResponseDto> responseDto = movieService.apiRequestForSearchMovie(apiMovieRequestDto);
        return ResponseUtils.success(responseDto);
    }

    /**
    * 영화 검색
    */
    @GetMapping("/search")
    public ResponseEntity<DataResponseDto<List<MovieGetResponseDto>>> getListMovie(
            @RequestBody MovieGetRequestDto movieGetRequestDto
    ) {
        log.info("In getMovie");
        List<MovieGetResponseDto> responseDtoList = movieService.getMovieList(movieGetRequestDto);
        return ResponseUtils.success(responseDtoList);
    }

    /**
    * 최신 인기 영화
    */
    @GetMapping("/trend")
    public ResponseEntity<DataResponseDto<List<MovieGetResponseDto>>> getMovieList(){
        log.info("In getMovieList");
//        movieService.getMovieList();
        return null;
    }

    @GetMapping("/creditSyncTest")
    public ResponseEntity<MessageResponseDto> getMovieTest(
            @RequestParam Long creditId
    ){
        movieService.creditSyncTest(creditId);
        return ResponseUtils.success();
    }
}