package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomFavoriteGeneratorTest {
    private static final List<Long> MOVIE_IDS = RandomReviewGeneratorTest.MOVIE_IDS;
    public static final int NUMBER_OF_FAVORITES = 400; // 생성할 찜하기 수
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
        generateFavorites(NUMBER_OF_FAVORITES, userIds, MOVIE_IDS, favorites, random);

        // 결과 출력
        System.out.println("INSERT INTO favorite (user_id, movie_id) VALUES");
        System.out.println(String.join(",\n", favorites) + ";");
        System.out.println("\n\n");
    }

    private void generateFavorites(int numberOfFavorites, List<Long> userIds, List<Long> movieIds,
        Set<String> favorites, Random random) {
        // Check if movieIds is empty before proceeding
        if (movieIds.isEmpty()) {
            throw new IllegalArgumentException("The movieIds list is empty, cannot generate favorites.");
        }

        Map<Long, Set<Long>> userMovieMap = new HashMap<>();

        while (favorites.size() < numberOfFavorites) {
            Long userId = getRandomElement(userIds, random);
            Long movieId = getRandomElement(movieIds, random);

            // 유저별로 찜한 영화 ID들을 관리하여 중복을 방지
            userMovieMap.putIfAbsent(userId, new HashSet<>());

            if (userMovieMap.get(userId).add(movieId)) { // 중복이 아니라면 추가
                String favoriteInsertKey = String.format("(%d, %d)", userId, movieId);
                favorites.add(favoriteInsertKey);
            }
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        if (list.isEmpty()) {
            throw new IllegalArgumentException("List is empty, cannot get a random element.");
        }
        return list.get(random.nextInt(list.size()));
    }
}