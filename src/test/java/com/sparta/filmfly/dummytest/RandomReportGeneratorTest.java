package com.sparta.filmfly.dummytest;

import com.sparta.filmfly.domain.report.entity.ReportTypeEnum;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomReportGeneratorTest {
    public static final int NUMBER_OF_REPORTS = 100; // 생성할 신고 수
    private static final int NUMBER_OF_USERS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_USER_RECORDS; // 생성할 유저 수
    private static final int DAYS_BEFORE = RandomEntityUserAndBoardAndCommentTest.DAYS_BEFORE; // 기준 날짜로부터 몇 일 전

    private static final int NUMBER_OF_REVIEWS = RandomReviewGeneratorTest.NUMBER_OF_REVIEWS; // 리뷰 수
    private static final int NUMBER_OF_BOARDS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_BOARD_RECORDS; // 보드 수
    private static final int NUMBER_OF_COMMENTS = RandomEntityUserAndBoardAndCommentTest.NUMBER_OF_COMMENT_RECORDS; // 댓글 수

    private static final Map<ReportTypeEnum, List<String>> REASONS_BY_TYPE = Map.of(
            ReportTypeEnum.BOARD, Arrays.asList(
                    "게시물에 부적절한 내용이 포함되어 있습니다.",
                    "게시물에서 욕설과 비방이 발견되었습니다.",
                    "게시물의 정보가 허위로 보입니다."
            ),
            ReportTypeEnum.REVIEW, Arrays.asList(
                    "리뷰의 내용이 과장되거나 사실과 다릅니다.",
                    "리뷰가 불쾌감을 주며 정확하지 않습니다.",
                    "리뷰의 일부 내용이 부적절합니다."
            ),
            ReportTypeEnum.COMMENT, Arrays.asList(
                    "댓글에 욕설이나 비방이 포함되어 있습니다.",
                    "댓글이 스팸 또는 광고로 보입니다.",
                    "댓글의 내용이 부적절하여 불쾌감을 줍니다."
            )
    );

    @Test
    public void generateRandomReports() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        List<Long> userIds = new ArrayList<>();
        for (long i = 1; i <= NUMBER_OF_USERS; i++) {
            userIds.add(i);
        }

        // 신고 데이터 생성
        Set<String> reports = new HashSet<>();
        generateReports(NUMBER_OF_REPORTS, userIds, reports, random, startDate, secondsBetween);

        // 결과 출력
        System.out.println("INSERT INTO report (reporter_id, reported_id, content, type_id, type, reason, created_at, updated_at) VALUES");
        System.out.println(String.join(",\n", reports) + ";");
        System.out.println("\n\n");
    }

    private void generateReports(int numberOfReports, List<Long> userIds, Set<String> reports, Random random,
                                 LocalDateTime startDate, long secondsBetween) {
        Set<String> uniqueReports = new HashSet<>();
        while (uniqueReports.size() < numberOfReports) {
            Long reporterId = getRandomElement(userIds, random);
            Long reportedId = getRandomElement(userIds, random);

            if (!reporterId.equals(reportedId)) { // 자기 자신을 신고하지 않도록 체크
                String reportKey = String.format("%d_%d", reporterId, reportedId);
                if (uniqueReports.add(reportKey)) { // 중복 체크 후 추가
                    ReportTypeEnum type = getRandomElement(Arrays.asList(ReportTypeEnum.values()), random);
                    String content = "원본 Content 내용";
                    Long typeId = getTypeIdByType(type, random); // type에 맞는 ID 생성
                    String reason = getRandomElement(REASONS_BY_TYPE.get(type), random);

                    // 생성시간과 수정시간 설정
                    LocalDateTime reportCreationDate = startDate.plusSeconds(random.nextInt((int) secondsBetween + 1));
                    LocalDateTime reportUpdateDate = reportCreationDate.plusDays(random.nextInt(10)); // 수정시간은 생성시간 이후 0~10일 사이

                    String formattedCreationDate = reportCreationDate.toString().replace("T", " ");
                    String formattedUpdateDate = reportUpdateDate.toString().replace("T", " ");

                    String reportInsertKey = String.format("(%d, %d, '%s', %d, '%s', '%s', '%s', '%s')",
                            reporterId, reportedId, content, typeId, type, reason, formattedCreationDate, formattedUpdateDate);
                    reports.add(reportInsertKey);
                }
            }
        }
    }

    private <T> T getRandomElement(List<T> list, Random random) {
        return list.get(random.nextInt(list.size()));
    }

    private Long getTypeIdByType(ReportTypeEnum type, Random random) {
        switch (type) {
            case BOARD:
                return (long) (random.nextInt(NUMBER_OF_BOARDS) + 1);
            case REVIEW:
                return (long) (random.nextInt(NUMBER_OF_REVIEWS) + 1);
            case COMMENT:
                return (long) (random.nextInt(NUMBER_OF_COMMENTS) + 1);
            default:
                throw new IllegalArgumentException("Unknown ReportTypeEnum: " + type);
        }
    }
}
