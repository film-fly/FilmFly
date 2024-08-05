package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomBlockGeneratorTest {
    public static final int NUMBER_OF_BLOCKS = 100; // 생성할 차단 수
    private static final int NUMBER_OF_USERS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_USER_RECORDS; // 생성할 유저 수
    private static final int DAYS_BEFORE = RandomEntityUserAndBoardAndCommentTest.DAYS_BEFORE; // 기준 날짜로부터 몇 일 전

    private static final List<String> MEMOS = Arrays.asList(
            "스팸 메시지 전송",
            "불쾌한 언행",
            "비매너 행동",
            "거래 사기",
            "욕설 사용",
            "광고성 메시지",
            "허위 정보 제공"
    );

    @Test
    public void generateRandomBlocks() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        List<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= NUMBER_OF_USERS; i++) {
            userIds.add(i);
        }

        // 차단 데이터 생성
        Set<String> blocks = new HashSet<>();
        generateBlocks(NUMBER_OF_BLOCKS, userIds, blocks, random, startDate, secondsBetween);

        // 결과 출력
        System.out.println("INSERT INTO block (blocker_id, blocked_id, memo, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", blocks) + ";");
        System.out.println("\n\n");
    }

    private void generateBlocks(int numberOfBlocks, List<Long> userIds, Set<String> blocks, Random random,
                                LocalDateTime startDate, long secondsBetween) {
        Set<String> uniqueBlocks = new HashSet<>();
        while (uniqueBlocks.size() < numberOfBlocks) {
            Long blockerId = getRandomElement(userIds, random);
            Long blockedId = getRandomElement(userIds, random);

            if (!blockerId.equals(blockedId)) { // 자기 자신을 차단하지 않도록 체크
                String blockKey = String.format("%d_%d", blockerId, blockedId);
                if (uniqueBlocks.add(blockKey)) { // 중복 체크 후 추가
                    String memo = MEMOS.get(random.nextInt(MEMOS.size()));

                    // 생성시간과 수정시간 설정
                    LocalDateTime blockCreationDate = startDate.plusSeconds(random.nextInt((int) secondsBetween + 1));
                    LocalDateTime blockUpdateDate = blockCreationDate.plusDays(random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

                    String formattedCreationDate = blockCreationDate.toString().replace("T", " ");
                    String formattedUpdateDate = blockUpdateDate.toString().replace("T", " ");

                    String blockInsertKey = String.format("(%d, %d, '%s', '%s', '%s')",
                            blockerId, blockedId, memo, formattedCreationDate, formattedUpdateDate);
                    blocks.add(blockInsertKey);
                }
            }
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }
}
