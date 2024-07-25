package com.sparta.filmfly.domain.movie.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.filmfly.domain.movie.dto.*;
import com.sparta.filmfly.domain.movie.entity.Credit;
import com.sparta.filmfly.domain.movie.entity.Movie;
import com.sparta.filmfly.domain.movie.entity.MovieCredit;
import com.sparta.filmfly.domain.movie.repository.CreditRepository;
import com.sparta.filmfly.domain.movie.repository.MovieCreditRepository;
import com.sparta.filmfly.domain.movie.repository.MovieRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.common.util.JsonFormatter;
import com.sparta.filmfly.global.exception.custom.detail.ApiRequestFailedException;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MovieService {

    private final MovieRepository movieRepository;
    private final CreditRepository creditRepository;
    private final MovieCreditRepository movieCreditRepository;
    private final OkHttpClient httpClient;

    @Value("${TMDB_API_AUTHORIZATION}")
    private String TMDB_API_AUTHORIZATION;

    public List<MovieGetResponseDto> getMovieList(MovieGetRequestDto movieGetRequestDto) {
        List<Movie> movieList;
        switch (movieGetRequestDto.getSearchType()) {
            case TITLE -> {
                movieList = movieRepository.findMoviesByTitle(movieGetRequestDto.getKeyword());
            }
//            case ACTOR -> {
//                movieList = movieRepository.findMoviesByTitle(movieGetRequestDto.getKeyword());
//            }
//            case PRODUCTION_COMPANY ->{
//                movieList = movieRepository.findMoviesByTitle(movieGetRequestDto.getKeyword());
//            }
            default -> System.out.println("default");
        }
        // 입력 가능 요소 :
        // DB 에 있는 데이터인지 확인
        // api 요청
        return null;
    }

    @Transactional
    public List<ApiMovieResponseDto> apiRequestForSearchMovie(ApiMovieRequestDto apiMovieRequestDto) {

        // 이미지 경로 : https://image.tmdb.org/t/p/w220_and_h330_face/이미지.jpg
        // 크기 : w220_and_h330_face , w600_and_h900_bestv2 , w1280
        String baseUrl = "https://api.themoviedb.org";
        String movieUrl = "/3/discover/movie";
//        String credits = "/3/movie/{movieId}/credits";

        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(baseUrl + movieUrl);
        Field[] fields = apiMovieRequestDto.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // private 필드 접근 허용
            try {
                Object value = field.get(apiMovieRequestDto);
                if (value != null) {
                    if (!(value instanceof Number && ((Number) value).doubleValue() == 0.0)) {
                        String fieldName = field.getName().replace("__", ".");
                        if (field.getType().isEnum()) {
                            // Enum 타입인 경우 getValue() 메서드를 호출하여 값을 쿼리 파라미터로 추가
                            Method getValueMethod = field.getType().getMethod("getValue");
                            Object enumValue = getValueMethod.invoke(value);
                            builder.queryParam(fieldName, enumValue);
                        } else {
                            // 일반 타입인 경우 값을 쿼리 파라미터로 추가
                            builder.queryParam(field.getName(), value);
                        }
                    }
                }
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        String url = builder.toUriString();
        log.info(url);
        Request movieRequest = requestBuilder(url);
        try (Response response = httpClient.newCall(movieRequest).execute()) {
            if (!response.isSuccessful()) throw new ApiRequestFailedException(ResponseCodeEnum.API_REQUEST_FAILED);
            String body = response.body().string();
            String format = JsonFormatter.formatJson(body);
            // log.info(format);

            // body 를 ApiMovieResponse 객체로 변환
            ObjectMapper objectMapper = new ObjectMapper();
            ApiMovieResponse apiMovieResponse = objectMapper.readValue(body, ApiMovieResponse.class);
            // 영화 api 조회 > 영화 목록 저장 및 업데이트 > 각 영화 > 배우 목록 저장 및 업데이트 > 중간 테이블 최신화 > 영화, 배우 테이블 동기화
            List<ApiMovieResponseDto> apiMovieResponseDtoList = apiMovieResponse.getResults();
            // 리스트 출력 확인
            List<Movie> movieList = apiMovieResponseDtoList.stream()
                    .map(ApiMovieResponseDto::toEntity)
                    .toList();
            movieList = movieRepository.saveAll(movieList);
            for (Movie movie : movieList) {
                // 영화 배우 목록 API 호출
                Request creditsRequest = requestBuilder(String.format("%s/3/movie/%d/credits", baseUrl, movie.getId()));
                Response creditsResponse = httpClient.newCall(creditsRequest).execute();
                if (!creditsResponse.isSuccessful())
                    throw new ApiRequestFailedException(ResponseCodeEnum.API_REQUEST_FAILED);
                // 배우 데이터 parsing
                String credits = creditsResponse.body().string();
                log.info(JsonFormatter.formatJson(credits));
                ApiCreditsResponse apiCreditsResponse = objectMapper.readValue(credits, ApiCreditsResponse.class);
                List<ApiCreditsResponseDto> apiCreditsResponseDtoList = apiCreditsResponse.getCast();

                // DTO 를 엔티티로 변환
                List<Credit> creditList = apiCreditsResponseDtoList.stream()
                        .map(ApiCreditsResponseDto::toEntity)
                        .collect(Collectors.toList());

                // Credit Entity 저장 : saveAll -> 이미 존재하는 데이터는 업데이트, 없으면 새로 생성
                creditList = creditRepository.saveAll(creditList);

                // MovieCredit 엔티티 생성 및 저장
                List<MovieCredit> movieCreditList = creditList.stream()
                        .map(credit -> {
                            Optional<MovieCredit> existingMovieCreditOpt = movieCreditRepository.findByMovieAndCredit(movie, credit);
                            return existingMovieCreditOpt.orElseGet(() -> MovieCredit.builder()
                                    .movie(movie)
                                    .credit(credit)
                                    .build());
                        })
                        .collect(Collectors.toList());
                movieCreditRepository.saveAll(movieCreditList);

                // Movie 의 movieCredits 리스트 업데이트 ->  CascadeAll 처리
//                movie.updateMovieCreditList(movieCreditList);
            }
            return apiMovieResponseDtoList;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Request requestBuilder(String url) {
        return new Request.Builder()
                .url(url)
                .get()
                .addHeader("accept", "application/json")
                .addHeader("Authorization", TMDB_API_AUTHORIZATION)
                .build();
    }

    private void updateMovieCreditListOnCredit(Credit credit) {
        List<MovieCredit> movieCreditList = movieCreditRepository.findByCredit(credit);
        credit.updateMovieCreditList(movieCreditList);
    }

    // C
    public void creditSyncTest(Long creditId) {
        Credit credit = creditRepository.findById(creditId).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.CREDIT_NOT_FOUND)
        );
        List<MovieCredit> movieCreditList = credit.getMovieCreditList();
        List<MovieCredit> movieCreditList2 = movieCreditRepository.findByCredit(credit);
        System.out.println(movieCreditList.size() + " & " + movieCreditList2.size());
    }
}