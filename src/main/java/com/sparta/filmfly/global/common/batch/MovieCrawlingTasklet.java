package com.sparta.filmfly.global.common.batch;

import com.sparta.filmfly.domain.movie.dto.ApiDiscoverMovieRequestDto;
import com.sparta.filmfly.domain.movie.service.MovieService;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

@Component
public class MovieCrawlingTasklet implements Tasklet {

    private final MovieService movieService;

    // 생성자 주입
    public MovieCrawlingTasklet(MovieService movieService) {
        this.movieService = movieService;
    }

    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        // 여러 번의 Discover API 호출
        for (int i = 0; i < 10; i++) {
            ApiDiscoverMovieRequestDto discoverRequestDto = ApiDiscoverMovieRequestDto.builder()
                    .page(i + 1)
                    .build();
            movieService.apiRequestForMovie(discoverRequestDto);
        }

        return RepeatStatus.FINISHED;
    }
}