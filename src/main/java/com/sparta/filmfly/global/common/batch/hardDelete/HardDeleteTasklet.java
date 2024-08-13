package com.sparta.filmfly.global.common.batch.hardDelete;

import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@AllArgsConstructor
public class HardDeleteTasklet<T> implements Tasklet {

    private final SoftDeletableRepository<T> repository;

    @Override
    public RepeatStatus execute(@NotNull StepContribution contribution, @NotNull ChunkContext chunkContext) throws Exception {

        // 삭제 기준일 (3개월 전의 날짜를 계산)
        LocalDateTime deletionThresholdDate = LocalDateTime.now().minusMonths(3);

        // deleted_at 필드가 삭제 기준일 이전인 엔티티들 찾기
        List<T> entitiesToDelete = repository.findAllByDeletedAtBefore(deletionThresholdDate);

        // 해당 엔티티들을 하드 딜리트
        repository.deleteAll(entitiesToDelete);

        return RepeatStatus.FINISHED;
    }
}
