package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomFavoriteGeneratorTest {
    private static final List<Long> MOVIE_IDS = RandomReviewGeneratorTest.MOVIE_IDS;
    public static final int NUMBER_OF_FAVORITES = 300; // 생성할 찜하기 수
    private static final int NUMBER_OF_USERS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_USER_RECORDS; // 생성할 유저 수
    private static final int DAYS_BEFORE = RandomEntityUserAndBoardAndCommentTest.DAYS_BEFORE; // 기준 날짜로부터 몇 일 전

    @Test
    public void generateRandomFavorites() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        List<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= NUMBER_OF_USERS; i++) {
            userIds.add(i);
        }

        // 찜하기 데이터 생성
        Set<String> favorites = new HashSet<>();
        generateFavorites(NUMBER_OF_FAVORITES, userIds, MOVIE_IDS, favorites, random, startDate, secondsBetween);

        // 결과 출력
        System.out.println("INSERT INTO favorite (user_id, movie_id) VALUES");
        System.out.println(String.join(",\n", favorites) + ";");
        System.out.println("\n\n");
    }

    private void generateFavorites(int numberOfFavorites, List<Long> userIds, List<Long> movieIds,
                                   Set<String> favorites, Random random, LocalDateTime startDate, long secondsBetween) {
        Set<String> uniqueFavorites = new HashSet<>();
        while (uniqueFavorites.size() < numberOfFavorites) {
            Long userId = getRandomElement(userIds, random);
            Long movieId = getRandomElement(movieIds, random);

            String favoriteKey = String.format("%d_%d", userId, movieId);
            if (uniqueFavorites.add(favoriteKey)) { // 중복 체크 후 추가
                String favoriteInsertKey = String.format("(%d, %d)", userId, movieId);
                favorites.add(favoriteInsertKey);
            }
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
