package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomReviewGeneratorTest {
    public static final List<Long> MOVIE_IDS = Arrays.asList(
            238L, 278L, 671L, 7451L, 10195L, 14836L, 77877L, 150540L, 263115L, 280180L, 293660L, 299536L,
            383498L, 383634L, 385687L, 437342L, 444483L, 447332L, 502356L, 519182L, 520763L, 533535L, 558144L,
            560016L, 567604L, 569094L, 573435L, 592695L, 603692L, 614933L, 634649L, 639720L, 646683L, 653346L,
            693134L, 704673L, 718821L, 719221L, 729165L, 746036L, 748783L, 762441L, 774531L, 786892L, 787699L,
            799583L, 821499L, 823464L, 857553L, 914206L, 929590L, 931405L, 938614L, 940551L, 940721L, 943344L,
            945961L, 955555L, 959098L, 974262L, 1001311L, 1010581L, 1010600L, 1011985L, 1012201L, 1014590L,
            1016346L, 1022789L, 1026819L, 1032823L, 1041613L, 1048241L, 1066262L, 1086747L, 1096197L, 1104844L,
            1114738L, 1115623L, 1117006L, 1130899L, 1143019L, 1147400L, 1152624L, 1159518L, 1174618L, 1179558L,
            1191610L, 1207898L, 1209290L, 1214509L, 1219685L, 1226578L, 1242372L, 1255350L, 1284004L, 1305095L,
            1308757L, 1309923L, 1311550L
    );
    public static final int NUMBER_OF_REVIEWS = 300; // 생성할 리뷰 수
    private static final int NUMBER_OF_USERS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_USER_RECORDS; // 생성할 유저 수
    private static final int DAYS_BEFORE = RandomEntityUserAndBoardAndCommentTest.DAYS_BEFORE; // 기준 날짜로부터 몇 일 전

    @Test
    public void generateRandomReviews() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        List<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= NUMBER_OF_USERS; i++) {
            userIds.add(i);
        }

        // 리뷰 데이터 생성
        Set<String> reviews = new HashSet<>();
        generateReviews(NUMBER_OF_REVIEWS, userIds, MOVIE_IDS, reviews, random, startDate, secondsBetween);

        // 결과 출력
        System.out.println("INSERT INTO review (user_id, movie_id, title, content, rating, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", reviews) + ";");
        System.out.println("\n\n");
    }

    private void generateReviews(int numberOfReviews, List<Long> userIds, List<Long> movieIds,
                                 Set<String> reviews, Random random, LocalDateTime startDate, long secondsBetween) {
        for (int i = 0; i < numberOfReviews; i++) {
            Long userId = getRandomElement(userIds, random);
            Long movieId = getRandomElement(movieIds, random);
            String title = "Review Title " + i;
            String content = "This is a review content for review number " + i;
            Float rating = (float) (1 + random.nextInt(5)); // Rating between 1 and 5

            // 생성시간과 수정시간 설정
            LocalDateTime reviewCreationDate = startDate.plusSeconds(random.nextInt((int) secondsBetween + 1));
            LocalDateTime reviewUpdateDate = reviewCreationDate.plusDays(random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

            String formattedCreationDate = reviewCreationDate.toString().replace("T", " ");
            String formattedUpdateDate = reviewUpdateDate.toString().replace("T", " ");

            String reviewKey = String.format("(%d, %d, '%s', '%s', %f, '%s', '%s')",
                    userId, movieId, title, content, rating, formattedCreationDate, formattedUpdateDate);

            reviews.add(reviewKey);
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
