package com.sparta.filmfly.global.common.batch.hardDelete;

import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface SoftDeletableRepository<T> {
    void deleteAllByDeletedAtBefore(LocalDateTime deletionThresholdDate);
}
