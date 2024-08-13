package com.sparta.filmfly.global.common.batch.crawling;

import com.sparta.filmfly.domain.movie.dto.ApiSearchMovieRequestDto;
import com.sparta.filmfly.domain.movie.entity.LanguagesEnum;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class MovieSearchTasklet implements Tasklet {
    private final RestTemplate restTemplate;

    public MovieSearchTasklet(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {
        ApiSearchMovieRequestDto searchRequestDto = ApiSearchMovieRequestDto.builder()
                .query("ave") // 필요한 파라미터 설정
                .includeAdult(false)
                .language(LanguagesEnum.KO_KR)
                .page(1)
                .build();

        // RestTemplate을 통해 Search API 호출
        restTemplate.postForEntity("http://localhost:8080/movies/api/search", searchRequestDto, Void.class);

        return RepeatStatus.FINISHED;
    }
}
