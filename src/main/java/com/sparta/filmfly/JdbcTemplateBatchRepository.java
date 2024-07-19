package com.sparta.filmfly;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import javax.sql.DataSource;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcTemplateBatchRepository {

    private final JdbcTemplate jdbcTemplate;

    public JdbcTemplateBatchRepository(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void batchInsertCards() {
        int totalDecks = 500;
        int recordsPerDeck = 3000;
        int batchSize = recordsPerDeck;
        int totalRecords = totalDecks * recordsPerDeck;

        List<Integer> deckIds = new ArrayList<>();
        List<Integer> nextIds = new ArrayList<>();
        List<Integer> userIds = new ArrayList<>();
        for (int i = 1; i <= 100; ++i) {
            deckIds.add(i);
        }
        for (int i = 1; i <= totalRecords; ++i) {
            nextIds.add(i);
        }
        for (int i = 1; i <= 500; ++i) {
            userIds.add(i);
        }
        Collections.shuffle(userIds);
        Collections.shuffle(nextIds);
        Collections.shuffle(deckIds);
        Random ran = new Random();

        String sql = "INSERT INTO card (due_date, start_date, created_at, updated_at, deck_id, next_id, user_id, description, title)"
            + " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        for (int deckId : deckIds) {
            List<Integer> currentDeckNextIds = nextIds.subList(deckId * recordsPerDeck - recordsPerDeck, deckId * recordsPerDeck);

            jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now());
                    ps.setTimestamp(1, timestamp);
                    ps.setTimestamp(2, timestamp);
                    ps.setTimestamp(3, timestamp);
                    ps.setTimestamp(4, timestamp);
                    ps.setInt(5, deckId);
                    ps.setInt(6, currentDeckNextIds.get(i));
                    ps.setInt(7, userIds.get(ran.nextInt(userIds.size())));
                    ps.setString(8, "제목" + (deckId * recordsPerDeck + i));
                    ps.setString(9, "내용" + (deckId * recordsPerDeck + i));
                }

                @Override
                public int getBatchSize() {
                    return batchSize;
                }
            });

            System.out.println("Deck ID: " + deckId + " 데이터 삽입 완료");
        }
    }
}