package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomReviewGeneratorTest {

    public static final List<Long> MOVIE_IDS = Arrays.asList(22L, 120L, 122L, 238L, 242L, 278L,
        348L, 597L, 664L, 671L, 673L, 7451L, 9820L, 10144L, 10192L, 10193L, 10195L, 14836L, 20352L,
        24428L, 36630L, 36647L, 38700L, 59053L, 76341L, 150540L, 157336L, 198663L, 263115L, 280180L,
        293660L, 298618L, 299534L, 299536L, 315162L, 346698L, 359410L, 383498L, 385687L, 436270L,
        437342L, 438148L, 438631L, 444483L, 447332L, 447365L, 453395L, 454619L, 502356L, 519182L,
        520763L, 533535L, 558144L, 560016L, 567604L, 568124L, 569094L, 573435L, 575264L, 592695L,
        601796L, 603692L, 609681L, 614933L, 615656L, 626412L, 634492L, 634649L, 635996L, 639720L,
        646683L, 653346L, 667538L, 693134L, 699172L, 704673L, 718821L, 719221L, 720321L, 739547L,
        746036L, 748783L, 762441L, 763215L, 774531L, 786892L, 787699L, 799583L, 821499L, 823464L,
        829402L, 831815L, 848326L, 857553L, 866398L, 872585L, 882059L, 893723L, 897087L, 914206L,
        920342L, 929590L, 931405L, 932086L, 933090L, 934632L, 935271L, 938614L, 940551L, 940721L,
        943344L, 945961L, 948549L, 955555L, 955916L, 959098L, 967582L, 967847L, 969492L, 974262L,
        974635L, 976573L, 980083L, 980489L, 984324L, 986843L, 996154L, 1001311L, 1010581L, 1010600L,
        1011985L, 1012201L, 1016346L, 1019317L, 1019411L, 1019420L, 1020896L, 1022789L, 1022796L,
        1023922L, 1026819L, 1026999L, 1029575L, 1030076L, 1032823L, 1041613L, 1048241L, 1062807L,
        1066262L, 1070535L, 1072790L, 1078249L, 1086591L, 1086747L, 1087388L, 1094844L, 1096197L,
        1097150L, 1104844L, 1107028L, 1111873L, 1114738L, 1115623L, 1117006L, 1126357L, 1129598L,
        1130899L, 1134433L, 1136318L, 1143019L, 1147400L, 1155089L, 1159518L, 1174618L, 1179558L,
        1189205L, 1191610L, 1197406L, 1207898L, 1209288L, 1209290L, 1214509L, 1219685L, 1226578L,
        1233486L, 1236671L, 1239083L, 1239251L, 1241674L, 1242372L, 1255350L, 1280768L, 1284004L,
        1291559L, 1308623L, 1308757L, 1309923L, 1311550L
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
        generateReviews(NUMBER_OF_REVIEWS, userIds, MOVIE_IDS, reviews, random, startDate,
            secondsBetween);

        // 결과 출력
        System.out.println(
            "INSERT INTO review (user_id, movie_id, title, content, rating, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", reviews) + ";");
        System.out.println("\n\n");
    }

    private void generateReviews(int numberOfReviews, List<Long> userIds, List<Long> movieIds,
        Set<String> reviews, Random random, LocalDateTime startDate, long secondsBetween) {
        for (int i = 0; i < numberOfReviews; i++) {
            Long userId = getRandomElement(userIds, random);
            Long movieId = getRandomElement(movieIds, random);
            float rating = 1 + random.nextInt(5); // 별점은 1~5

            // 리뷰 제목과 내용을 별점에 따라 생성
            String title = generateReviewTitle(rating, random);
            String content = generateReviewContent(rating, random);

            // 생성시간과 수정시간 설정
            LocalDateTime reviewCreationDate = startDate.plusSeconds(
                random.nextInt((int) secondsBetween + 1));
            LocalDateTime reviewUpdateDate = reviewCreationDate.plusDays(
                random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

            String formattedCreationDate = reviewCreationDate.toString().replace("T", " ");
            String formattedUpdateDate = reviewUpdateDate.toString().replace("T", " ");

            String reviewKey = String.format("(%d, %d, '%s', '%s', %.1f, '%s', '%s')",
                userId, movieId, title, content, rating, formattedCreationDate,
                formattedUpdateDate);

            reviews.add(reviewKey);
        }
    }

    private String generateReviewTitle(float rating, Random random) {
        // 별점에 따라 다양한 제목을 생성
        switch ((int) rating) {
            case 5:
                return getRandomElement(Arrays.asList(
                    "최고의 영화!", "이 영화는 꼭 봐야 해요!", "완벽한 영화 경험", "가장 추천하는 영화입니다!", "이 영화를 놓치지 마세요!"
                ), random);
            case 4:
                return getRandomElement(Arrays.asList(
                    "좋은 영화입니다", "볼만한 영화", "즐겁게 볼 수 있는 영화", "추천할 만한 영화", "괜찮은 영화입니다"
                ), random);
            case 3:
                return getRandomElement(Arrays.asList(
                    "그냥 그렇습니다", "평범한 영화", "무난한 영화", "다시 볼 필요는 없어요", "기대 이하였습니다"
                ), random);
            case 2:
                return getRandomElement(Arrays.asList(
                    "실망스러운 영화", "별로 추천하지 않습니다", "아쉬운 영화", "시간 낭비", "다시 보고 싶지 않아요"
                ), random);
            case 1:
                return getRandomElement(Arrays.asList(
                    "최악의 영화", "정말로 보기 싫었던 영화", "시간을 낭비했습니다", "이 영화는 추천하지 않습니다", "참혹한 경험이었습니다"
                ), random);
            default:
                return "리뷰 제목 없음";
        }
    }

    private String generateReviewContent(float rating, Random random) {
        // 별점에 따라 다양한 내용 생성
        switch ((int) rating) {
            case 5:
                return getRandomElement(Arrays.asList(
                    "이 영화는 정말 훌륭했습니다! 모든 면에서 완벽했어요. 강력히 추천합니다.",
                    "감동적인 스토리와 훌륭한 연기! 다시 보고 싶네요.",
                    "완벽한 영화입니다. 최고로 추천할 만한 영화입니다!",
                    "모든 것이 뛰어난 영화입니다. 놓치지 마세요!",
                    "정말 최고였어요. 다시 볼 준비가 되어 있습니다."
                ), random);
            case 4:
                return getRandomElement(Arrays.asList(
                    "좋은 영화였습니다. 몇 가지 아쉬운 점이 있지만 전반적으로 만족스러웠습니다.",
                    "전반적으로 괜찮은 영화였지만, 몇 군데 개선이 필요해 보입니다.",
                    "스토리와 연기가 좋았지만, 약간의 아쉬움이 있었습니다.",
                    "즐겁게 볼 수 있는 영화였으나, 몇 가지 아쉬운 점이 있었습니다.",
                    "보통 수준의 영화였지만, 여전히 볼만했습니다."
                ), random);
            case 3:
                return getRandomElement(Arrays.asList(
                    "보통 수준의 영화입니다. 특별히 기억에 남는 점은 없었습니다.",
                    "그저 그런 영화였어요. 큰 감동은 없었습니다.",
                    "볼만했지만, 큰 인상은 남지 않았습니다.",
                    "기대보다 평범한 영화였어요. 재밌었지만 특별할 건 없었습니다.",
                    "전체적으로 무난했지만, 특별히 추천할 만한 영화는 아닙니다."
                ), random);
            case 2:
                return getRandomElement(Arrays.asList(
                    "많이 실망스러웠습니다. 별로 추천하고 싶지 않네요.",
                    "기대 이하의 영화였습니다. 많은 개선이 필요해 보입니다.",
                    "이 영화는 많이 아쉬웠습니다. 다시 보고 싶지 않네요.",
                    "내용이 부족하고 재미가 없었습니다. 추천하지 않습니다.",
                    "많은 부분에서 실망스러웠습니다. 시간이 아까웠어요."
                ), random);
            case 1:
                return getRandomElement(Arrays.asList(
                    "정말 최악의 영화였습니다. 시간이 낭비였어요.",
                    "이 영화는 정말 최악이었어요. 다시는 보고 싶지 않아요.",
                    "매우 실망스러운 영화였습니다. 전혀 추천하지 않습니다.",
                    "참혹한 경험이었습니다. 시간과 돈이 아까웠어요.",
                    "이 영화는 완전히 실패작입니다. 절대 보지 마세요."
                ), random);
            default:
                return "리뷰 내용 없음";
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}