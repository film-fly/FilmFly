package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomCollectionGeneratorTest {
    private static final List<Long> MOVIE_IDS = RandomReviewGeneratorTest.MOVIE_IDS;
    public static final int NUMBER_OF_COLLECTIONS = 100; // 생성할 컬렉션 수
    private static final int MIN_MOVIES_PER_COLLECTION = 0; // 컬렉션당 최소 영화 수
    private static final int MAX_MOVIES_PER_COLLECTION = 15; // 컬렉션당 최대 영화 수
    private static final int NUMBER_OF_USERS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_USER_RECORDS; // 생성할 유저 수
    private static final int DAYS_BEFORE = RandomEntityUserAndBoardAndCommentTest.DAYS_BEFORE; // 기준 날짜로부터 몇 일 전

    private static final List<String> COLLECTION_NAMES = Arrays.asList(
            "나의 영화 보관함", "최애 영화 리스트", "볼만한 영화", "추천 영화", "명작 모음",
            "기억에 남는 영화", "다시 보고 싶은 영화", "친구에게 추천할 영화", "특별한 영화", "영화 컬렉션",
            "액션 영화 베스트", "로맨스 영화 필름", "SF & 판타지 추천", "가족과 함께 보기 좋은 영화",
            "드라마 & 감동 영화", "최근 개봉작", "히어로 영화 모음", "클래식 영화", "인디 영화 추천",
            "애니메이션 필름"
    );

    private static final List<String> COLLECTION_CONTENTS = Arrays.asList(
            "이 보관함에는 내가 좋아하는 영화들이 모여 있어요.",
            "다시 보고 싶은 영화들을 모아둔 리스트입니다.",
            "친구들에게 추천할 만한 영화 모음입니다.",
            "감동적인 영화들만 모아 놓은 보관함입니다.",
            "한 번쯤 꼭 봐야 할 영화들입니다.",
            "액션 장르에서 최고로 평가받는 영화들을 모아봤습니다. 스릴과 박진감 넘치는 액션을 즐기세요!",
            "로맨스 장르에서 감동적이고 아름다운 영화들을 모았습니다. 사랑을 느껴보세요.",
            "SF와 판타지 장르의 환상적인 영화들을 추천합니다. 상상력과 모험이 가득합니다.",
            "가족과 함께 즐기기 좋은 영화들을 모아봤습니다. 모두가 함께 즐길 수 있는 영화들입니다.",
            "드라마와 감동적인 이야기로 가득한 영화들을 모았습니다. 진한 감동을 경험해보세요.",
            "최근에 개봉한 최신 영화를 모아봤습니다. 최신 트렌드를 놓치지 마세요.",
            "영웅들의 이야기를 담은 영화들을 모았습니다. 히어로와의 모험을 즐기세요.",
            "영화의 역사에서 중요한 클래식 영화들을 모았습니다. 시대를 초월한 명작을 감상하세요.",
            "독립 영화에서 찾아볼 수 있는 독특하고 신선한 영화들을 추천합니다. 새로운 경험을 해보세요.",
            "애니메이션의 매력적인 세계를 탐험하는 영화들을 모았습니다. 색다른 재미와 감동을 느껴보세요."
    );

    @Test
    public void generateRandomCollections() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        List<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= NUMBER_OF_USERS; i++) {
            userIds.add(i);
        }

        // 컬렉션 데이터 생성
        Set<String> collections = new HashSet<>();
        Set<String> movieCollections = new HashSet<>();
        generateCollections(NUMBER_OF_COLLECTIONS, userIds, MOVIE_IDS, collections, movieCollections, random, startDate, secondsBetween);

        // 결과 출력
        System.out.println("INSERT INTO collection (user_id, name, content, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", collections) + ";");

        System.out.println("\n\nINSERT INTO movie_collection (collection_id, movie_id, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", movieCollections) + ";");
        System.out.println("\n\n");
    }

    private void generateCollections(int numberOfCollections, List<Long> userIds, List<Long> movieIds,
                                     Set<String> collections, Set<String> movieCollections, Random random, LocalDateTime startDate, long secondsBetween) {
        Map<Long, Set<String>> userCollectionNames = new HashMap<>();
        for (Long userId : userIds) {
            userCollectionNames.put(userId, new HashSet<>());
        }

        for (int i = 0; i < numberOfCollections; i++) {
            Long userId = getRandomElement(userIds, random);
            Set<String> usedNames = userCollectionNames.get(userId);

            String collectionName = generateUniqueName(usedNames, random);
            usedNames.add(collectionName);

            String collectionContent = getRandomElement(COLLECTION_CONTENTS, random);

            // 생성시간과 수정시간 설정
            LocalDateTime collectionCreationDate = startDate.plusSeconds(random.nextInt((int) secondsBetween + 1));
            LocalDateTime collectionUpdateDate = collectionCreationDate.plusDays(random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

            String formattedCreationDate = collectionCreationDate.toString().replace("T", " ");
            String formattedUpdateDate = collectionUpdateDate.toString().replace("T", " ");

            String collectionInsertKey = String.format("(%d, '%s', '%s', '%s', '%s')",
                    userId, collectionName, collectionContent, formattedCreationDate, formattedUpdateDate);
            collections.add(collectionInsertKey);

            Long collectionId = (long) i + 1; // 임시로 컬렉션 ID 할당

            // 영화 컬렉션 데이터 생성
            int moviesPerCollection = random.nextInt(MAX_MOVIES_PER_COLLECTION - MIN_MOVIES_PER_COLLECTION + 1) + MIN_MOVIES_PER_COLLECTION;
            Set<String> uniqueMovies = new HashSet<>();
            while (uniqueMovies.size() < moviesPerCollection) {
                Long movieId = getRandomElement(movieIds, random);
                if (uniqueMovies.add(movieId.toString())) {
                    // 생성시간과 수정시간 설정
                    LocalDateTime movieCollectionCreationDate = startDate.plusSeconds(random.nextInt((int) secondsBetween + 1));
                    LocalDateTime movieCollectionUpdateDate = movieCollectionCreationDate.plusDays(random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

                    String formattedMovieCollectionCreationDate = movieCollectionCreationDate.toString().replace("T", " ");
                    String formattedMovieCollectionUpdateDate = movieCollectionUpdateDate.toString().replace("T", " ");

                    String movieCollectionInsertKey = String.format("(%d, %d, '%s', '%s')",
                            collectionId, movieId, formattedMovieCollectionCreationDate, formattedMovieCollectionUpdateDate);
                    movieCollections.add(movieCollectionInsertKey);
                }
            }
        }
    }

    private String generateUniqueName(Set<String> usedNames, Random random) {
        String baseName = getRandomElement(COLLECTION_NAMES, random);
        String uniqueName = baseName;
        int counter = 1;

        while (usedNames.contains(uniqueName)) {
            uniqueName = baseName + " " + counter++;
        }

        return uniqueName;
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
