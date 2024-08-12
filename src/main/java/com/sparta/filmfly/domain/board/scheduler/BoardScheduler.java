package com.sparta.filmfly.domain.board.scheduler;

import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.global.util.RedisLockUtils;
import jakarta.persistence.EntityManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class BoardScheduler {

    private static final String BOARD_HITS_PREFIX = "board:hits:";
    private static final String SCHEDULER_LOCK_KEY = "scheduler:lock:board";

    private final BoardRepository boardRepository;
    private final RedisLockUtils redisLockUtils;
    private final RedisTemplate<String, String> redisTemplate;
    private final JdbcTemplate jdbcTemplate;
    private final EntityManager em;

    @Scheduled(cron = "0 0 4 * * ?") // 새벽 4시에 실행
    @Transactional
    public void updateBoardHits() {
        if (redisLockUtils.acquireLock(SCHEDULER_LOCK_KEY)) {
            Long lastBoardId = 0L;
            int batchSize = 1000;

            while (true) {
                List<Board> boards = boardRepository
                    .findByIdGreaterThan(
                        lastBoardId, PageRequest.of(0, batchSize)
                    );

                if (boards.isEmpty()) {
                    break;
                }

                List<List<Long>> batchBoards = new ArrayList<>();

                for (Board board : boards) {
                    em.detach(board); // 더티체크 방지

                    String key = BOARD_HITS_PREFIX + board.getId();
                    String redisHits = redisTemplate.opsForValue().get(key);

                    if (StringUtils.hasText(redisHits)) {
                        Long viewCount = Long.parseLong(redisHits);
                        board.updateHits(viewCount);

                        batchBoards.add(List.of(viewCount, board.getId()));
                    }
                }

                String sql = "UPDATE board SET hits = ? WHERE id = ?";
                jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setLong(1, batchBoards.get(i).get(0)); // hits
                        ps.setLong(2, batchBoards.get(i).get(1)); // id
                    }

                    @Override
                    public int getBatchSize() {
                        return batchBoards.size();
                    }
                });

                // 마지막으로 처리된 board id 갱신
                lastBoardId = boards.get(boards.size() - 1).getId();
            }
        }
    }
}