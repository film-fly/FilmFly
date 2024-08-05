package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomEntityUserAndBoardAndCommentTest {
    private static final String[] FIXED_NICKNAMES = {
            "원지연", "백원하", "이은규", "한호진", "강준모"
    };
    private static final String FIXED_PASSWORD = "$2a$12$rQpJST/20h27oYcjOZ20XOqusfj5O.x2u9W1nnZ9RYdZWYU3IQwxu";
    public static final int NUMBER_OF_USER_RECORDS = 20; // 생성할 유저 레코드 수
    public static final int NUMBER_OF_BOARD_RECORDS = 100; // 생성할 보드 레코드 수
    public static final int NUMBER_OF_COMMENT_RECORDS = 100; // 생성할 댓글 레코드 수
    public static final int DAYS_BEFORE = 30; // 기준 날짜로부터 몇 일 전

    @Test
    public void testDataGeneration() {
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = now.minusDays(DAYS_BEFORE); // 오늘 날짜 기준 DAYS_BEFORE 일 전
        long secondsBetween = ChronoUnit.SECONDS.between(startDate, now);

        // 유저 데이터 생성
        Set<String> usedNicknames = new HashSet<>();
        StringBuilder userSb = new StringBuilder();
        LocalDateTime[] userCreationDates = new LocalDateTime[NUMBER_OF_USER_RECORDS];

        LocalDateTime previousUserDate = startDate;
        for (int i = 1; i <= NUMBER_OF_USER_RECORDS; i++) {
            long randomSeconds = random.nextInt((int) secondsBetween + 1);
            LocalDateTime userCreationDate = startDate.plusSeconds(randomSeconds);

            int randomHour = random.nextInt(24);
            int randomMinute = random.nextInt(60);
            int randomSecond = random.nextInt(60);
            userCreationDate = userCreationDate.withHour(randomHour)
                    .withMinute(randomMinute)
                    .withSecond(randomSecond);

            int randomMillis = random.nextInt(1000);
            int randomNanos = random.nextInt(1_000_000_000);
            userCreationDate = userCreationDate.plusNanos(randomMillis * 1_000_000 + randomNanos);

            if (userCreationDate.isBefore(previousUserDate)) {
                userCreationDate = previousUserDate.plusSeconds(1);
            }

            previousUserDate = userCreationDate;

            String nickname;
            String username;
            String role;
            if (i <= 5) {
                // admin 계정 설정
                username = "admin" + i;
                nickname = FIXED_NICKNAMES[i - 1];
                role = "ROLE_ADMIN";
            } else {
                username = "username" + i;
                nickname = (i <= FIXED_NICKNAMES.length)
                        ? FIXED_NICKNAMES[i - 1]
                        : generateUniqueKoreanName(random, usedNicknames);
                role = "ROLE_USER";
            }
            usedNicknames.add(nickname);

            String formattedDateTime = userCreationDate.toString().replace("T", " ");
            userSb.append(String.format(
                    "('%s', '%s', 'user%d@example.com', '%s', NULL, 'http://example.com/pic%d.jpg', 'ACTIVE', '%s', '%s', '%s')",
                    username, FIXED_PASSWORD, i, nickname, i, role, formattedDateTime, formattedDateTime
            ));

            if (i < NUMBER_OF_USER_RECORDS) {
                userSb.append(",\n");
            }

            userCreationDates[i - 1] = userCreationDate;
        }

        // 보드 데이터 생성
        List<BoardData> boardDataList = new ArrayList<>();

        for (int i = 1; i <= NUMBER_OF_BOARD_RECORDS; i++) {
            // 랜덤 유저 선택
            int userIndex = random.nextInt(NUMBER_OF_USER_RECORDS);
            LocalDateTime userCreationDate = userCreationDates[userIndex];

            // 보드 생성일자를 유저 생성일자보다 하루 뒤로 설정
            LocalDateTime boardCreationDate = userCreationDate.plusDays(1);

            int randomHour = random.nextInt(24);
            int randomMinute = random.nextInt(60);
            int randomSecond = random.nextInt(60);
            boardCreationDate = boardCreationDate.withHour(randomHour)
                    .withMinute(randomMinute)
                    .withSecond(randomSecond);

            int randomMillis = random.nextInt(1000);
            int randomNanos = random.nextInt(1_000_000_000);
            boardCreationDate = boardCreationDate.plusNanos(randomMillis * 1_000_000 + randomNanos);

            String formattedDateTime = boardCreationDate.toString().replace("T", " ");
            long userId = userIndex + 1; // 유저 ID는 1부터 시작

            // 랜덤 hits 값 (0~1000)
            long randomHits = random.nextInt(1001); // 0부터 1000까지의 랜덤 숫자

            boardDataList.add(new BoardData(userId, i, formattedDateTime, formattedDateTime, randomHits));
        }

        // 보드 데이터를 생성일자 기준으로 정렬
        boardDataList.sort(Comparator.comparing(BoardData::getCreatedAt));

        StringBuilder boardSb = new StringBuilder();
        for (int i = 0; i < boardDataList.size(); i++) {
            BoardData boardData = boardDataList.get(i);
            boardSb.append(String.format(
                    "(%d, '제목 %d', '내용 %d', '%s', '%s', %d)",
                    boardData.getUserId(), boardData.getTitle(), boardData.getTitle(),
                    boardData.getCreatedAt(), boardData.getUpdatedAt(), boardData.getHits()
            ));

            if (i < boardDataList.size() - 1) {
                boardSb.append(",\n");
            }
        }

        // 댓글 데이터 생성
        List<CommentData> commentDataList = new ArrayList<>();

        for (int i = 1; i <= NUMBER_OF_COMMENT_RECORDS; i++) {
            // 랜덤 보드 선택
            int boardIndex = random.nextInt(NUMBER_OF_BOARD_RECORDS);
            BoardData boardData = boardDataList.get(boardIndex);

            // 랜덤 유저 선택
            int userIndex = random.nextInt(NUMBER_OF_USER_RECORDS);

            // 보드 생성일자를 LocalDateTime으로 변환
            LocalDateTime boardCreationDate = LocalDateTime.parse(boardData.getCreatedAt().replace(" ", "T"));

            // 댓글 생성일자를 보드 생성일자 이후로 설정
            LocalDateTime commentCreationDate = boardCreationDate.plusSeconds(i * 100); // 시간 간격을 두고 생성

            int randomHour = commentCreationDate.getHour();
            int randomMinute = commentCreationDate.getMinute();
            int randomSecond = commentCreationDate.getSecond();
            commentCreationDate = commentCreationDate.withHour(randomHour)
                    .withMinute(randomMinute)
                    .withSecond(randomSecond);

            int randomMillis = random.nextInt(1000);
            int randomNanos = random.nextInt(1_000_000_000);
            commentCreationDate = commentCreationDate.plusNanos(randomMillis * 1_000_000 + randomNanos);

            String formattedDateTime = commentCreationDate.toString().replace("T", " ");
            long userId = userIndex + 1; // 유저 ID는 1부터 시작

            commentDataList.add(new CommentData(userId, boardData.getBoardId(), "댓글 내용 " + i, formattedDateTime));
        }

        // 댓글 데이터를 생성일자 기준으로 정렬
        commentDataList.sort(Comparator.comparing(CommentData::getCreatedAt));

        StringBuilder commentSb = new StringBuilder();
        for (int i = 0; i < commentDataList.size(); i++) {
            CommentData commentData = commentDataList.get(i);
            commentSb.append(String.format(
                    "(%d, %d, '%s', '%s', '%s')",
                    commentData.getUserId(), commentData.getBoardId(), commentData.getContent(), commentData.getCreatedAt(), commentData.getCreatedAt()
            ));

            if (i < commentDataList.size() - 1) {
                commentSb.append(",\n");
            }
        }

        // 결과 출력
        System.out.println("INSERT INTO user (username, password, email, nickname, kakao_id, picture_url, user_status, user_role, created_at, updated_at) VALUES");
        System.out.println(userSb.toString() + ";");
        System.out.println("\n\n");

        System.out.println("INSERT INTO board (user_id, title, content, created_at, updated_at, hits) VALUES");
        System.out.println(boardSb.toString() + ";");
        System.out.println("\n\n");

        System.out.println("INSERT INTO comment (user_id, board_id, content, created_at, updated_at) VALUES");
        System.out.println(commentSb.toString() + ";");
        System.out.println("\n\n");
    }

    private String generateUniqueKoreanName(Random random, Set<String> usedNicknames) {
        String nickname;
        do {
            nickname = generateRandomKoreanName(random);
        } while (usedNicknames.contains(nickname));
        return nickname;
    }

    private String generateRandomKoreanName(Random random) {
        String[] familyNames = {"김", "이", "박", "정", "최", "원", "강", "한", "백"};
        String[] givenNames = {"가", "나", "다", "라", "마", "바", "사", "아", "자", "차", "카", "타", "파", "하", "지", "연", "준", "모", "원", "하", "은", "규", "호", "진"};

        String familyName = familyNames[random.nextInt(familyNames.length)];
        String givenName = givenNames[random.nextInt(givenNames.length)] +
                givenNames[random.nextInt(givenNames.length)];

        return familyName + givenName;
    }

    // 보드 데이터를 담기 위한 내부 클래스
    static class BoardData {
        private final long userId;
        private final int title;
        private final String content;
        private final String createdAt;
        private final String updatedAt;
        private final long hits;
        private final long boardId;

        public BoardData(long userId, long boardId, String createdAt, String updatedAt, long hits) {
            this.userId = userId;
            this.boardId = boardId;
            this.title = (int) boardId;
            this.content = "내용 " + title; // content는 title에 따라 다름
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.hits = hits;
        }

        public long getUserId() {
            return userId;
        }

        public int getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public long getHits() {
            return hits;
        }

        public long getBoardId() {
            return boardId;
        }
    }

    // 댓글 데이터를 담기 위한 내부 클래스
    static class CommentData {
        private final long userId;
        private final long boardId;
        private final String content;
        private final String createdAt;

        public CommentData(long userId, long boardId, String content, String createdAt) {
            this.userId = userId;
            this.boardId = boardId;
            this.content = content;
            this.createdAt = createdAt;
        }

        public long getUserId() {
            return userId;
        }

        public long getBoardId() {
            return boardId;
        }

        public String getContent() {
            return content;
        }

        public String getCreatedAt() {
            return createdAt;
        }
    }
}
