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
    public static final int NUMBER_OF_USER_RECORDS = 25; // 생성할 유저 레코드 수
    public static final int NUMBER_OF_BOARD_RECORDS = 100; // 생성할 보드 레코드 수
    public static final int NUMBER_OF_COMMENT_RECORDS = 300; // 생성할 댓글 레코드 수
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
                username = "username" + (i - 5);
                nickname = (i <= FIXED_NICKNAMES.length)
                        ? FIXED_NICKNAMES[i - 1]
                        : generateUniqueKoreanName(random, usedNicknames);
                role = "ROLE_USER";
            }
            usedNicknames.add(nickname);

            String formattedDateTime = userCreationDate.toString().replace("T", " ");
            userSb.append(String.format(
                    "('%s', '%s', 'user%d@example.com', '%s', NULL, NULL, 'ACTIVE', '%s', '%s', '%s')",
                    username, FIXED_PASSWORD, i, nickname, role, formattedDateTime, formattedDateTime
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

            // 보드 제목과 내용에 한글 추가
            String title = getRandomBoardTitle(random);
            String content = getRandomBoardContent(random);

            boardDataList.add(new BoardData(userId, i, title, content, formattedDateTime, formattedDateTime, randomHits));
        }

        // 보드 데이터를 생성일자 기준으로 정렬
        boardDataList.sort(Comparator.comparing(BoardData::getCreatedAt));

        StringBuilder boardSb = new StringBuilder();
        for (int i = 0; i < boardDataList.size(); i++) {
            BoardData boardData = boardDataList.get(i);
            boardSb.append(String.format(
                    "(%d, '%s', '%s', '%s', '%s', %d)",
                    boardData.getUserId(), boardData.getTitle(), boardData.getContent(),
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

            // 댓글 내용에 한글 추가
            String commentContent = getRandomCommentContent(random);

            commentDataList.add(new CommentData(userId, boardData.getBoardId(), commentContent, formattedDateTime));
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

    private String getRandomBoardTitle(Random random) {
        String[] titles = {
                "안녕하세요", "새로운 소식", "이벤트 안내", "업데이트 정보", "공지사항",
                "다시 시작합니다", "우리 팀 소개", "자주 묻는 질문", "고객 지원", "문의하기",
                "이번 달 특별 행사", "시스템 유지 보수", "서비스 점검", "특별 할인", "새로운 기능 소개",
                "프로젝트 발표", "연말 결산", "신제품 출시", "회원 혜택", "주간 리뷰",
                "캠페인 시작", "참여 이벤트", "서비스 개선 안내", "제휴 소식", "공식 발표",
                "트렌드 분석", "업계 뉴스", "정기 업데이트", "서비스 리뉴얼", "사용자 가이드"
        };
        return titles[random.nextInt(titles.length)];
    }

    private String getRandomBoardContent(Random random) {
        String[] contents = {
                "이번 주에는 특별한 이벤트가 준비되어 있습니다. 많은 참여 부탁드립니다.",
                "새로운 기능이 추가되었습니다. 자세한 내용은 공지사항을 확인해주세요.",
                "시스템 점검이 예정되어 있습니다. 이용에 불편을 드려 죄송합니다.",
                "고객 지원팀에 문의하시면 빠르게 답변을 드리겠습니다.",
                "특별 할인을 진행 중입니다. 자세한 내용은 홈페이지를 참고해주세요.",
                "서비스 개선을 위한 피드백을 기다리고 있습니다. 소중한 의견을 남겨주세요.",
                "이번 달의 추천 상품을 소개합니다. 많은 관심 부탁드립니다.",
                "자주 묻는 질문을 업데이트했습니다. 도움이 되시길 바랍니다.",
                "저희 팀의 새로운 프로젝트를 소개합니다. 많은 관심 부탁드립니다.",
                "문의 사항이 있으시면 언제든지 연락주시면 감사하겠습니다."
        };
        return contents[random.nextInt(contents.length)];
    }

    private String getRandomCommentContent(Random random) {
        String[] comments = {
                "정말 유용한 정보 감사합니다!", "이벤트 참여해볼게요.", "서비스가 점점 좋아지네요.",
                "고객 지원팀의 답변이 빠릅니다.", "할인 정보를 감사합니다.", "문의 사항이 해결되었습니다.",
                "팀 소개를 보니 믿음이 가네요.", "자주 묻는 질문이 도움이 되었습니다.", "새로운 기능이 기대됩니다.",
                "서비스 유지 보수에 대한 안내 감사합니다.", "기대 이상입니다!", "정말 잘 설명해주셨습니다.",
                "이번 달의 추천 상품 좋네요.", "서비스가 계속 발전하는군요.", "문의하기가 쉽네요."
        };
        return comments[random.nextInt(comments.length)];
    }

    // 보드 데이터를 담기 위한 내부 클래스
    static class BoardData {
        private final long userId;
        private final long boardId;
        private final String title;
        private final String content;
        private final String createdAt;
        private final String updatedAt;
        private final long hits;

        public BoardData(long userId, long boardId, String title, String content, String createdAt, String updatedAt, long hits) {
            this.userId = userId;
            this.boardId = boardId;
            this.title = title; // 타이틀을 문자열로 사용
            this.content = content;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
            this.hits = hits;
        }

        public long getUserId() {
            return userId;
        }

        public long getBoardId() {
            return boardId;
        }

        public String getTitle() {
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