package com.sparta.filmfly.dummytest;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

class RandomReviewGeneratorTest {

    public static final List<Long> MOVIE_IDS = Arrays.asList(11L, 12L, 13L, 18L, 22L, 24L, 38L, 58L, 62L, 78L,
        85L, 98L, 101L, 103L, 105L, 106L, 111L, 114L, 118L, 120L, 121L, 122L, 128L, 129L, 155L, 161L, 162L, 169L, 185L,
        189L, 194L, 197L, 218L, 235L, 238L, 240L, 242L, 251L, 252L, 254L, 268L, 272L, 275L, 278L, 285L, 297L, 346L,
        348L, 350L, 389L, 411L, 423L, 424L, 425L, 429L, 489L, 497L, 510L, 531L, 550L, 557L, 559L, 578L, 585L, 591L,
        594L, 597L, 598L, 600L, 602L, 603L, 604L, 605L, 607L, 608L, 615L, 628L, 630L, 640L, 646L, 664L, 671L, 672L,
        673L, 674L, 675L, 679L, 680L, 694L, 744L, 745L, 764L, 767L, 769L, 772L, 807L, 808L, 809L, 812L, 817L, 841L,
        854L, 857L, 862L, 863L, 869L, 871L, 920L, 948L, 949L, 950L, 953L, 957L, 966L, 967L, 979L, 1018L, 1024L, 1091L,
        1124L, 1250L, 1366L, 1368L, 1369L, 1370L, 1417L, 1422L, 1427L, 1495L, 1572L, 1573L, 1579L, 1607L, 1677L,
        1701L, 1712L, 1724L, 1726L, 1734L, 1735L, 1771L, 1813L, 1824L, 1825L, 1865L, 1885L, 1891L, 1893L, 1895L,
        1924L, 1930L, 1948L, 1949L, 1979L, 2062L, 2105L, 2109L, 2322L, 2619L, 2668L, 2758L, 2770L, 3170L, 4011L,
        4232L, 4233L, 4247L, 4248L, 4257L, 4348L, 4935L, 4951L, 5174L, 5175L, 6479L, 6844L, 6977L, 7214L, 7451L,
        7485L, 7555L, 8273L, 8355L, 8358L, 8363L, 8392L, 8467L, 8587L, 8681L, 8835L, 8869L, 8920L, 8961L, 8966L,
        9016L, 9023L, 9303L, 9325L, 9353L, 9358L, 9471L, 9487L, 9552L, 9603L, 9645L, 9693L, 9702L, 9737L, 9738L,
        9739L, 9757L, 9806L, 9820L, 9836L, 9880L, 10009L, 10014L, 10020L,
        10022L, 10066L, 10138L, 10140L, 10141L, 10144L, 10159L, 10191L, 10192L, 10193L, 10195L, 10196L, 10198L,
        10201L, 10202L, 10212L, 10216L, 10218L, 10220L, 10229L, 10234L, 10238L, 10246L, 10249L, 10307L, 10312L,
        10315L, 10320L, 10327L, 10330L, 10331L, 10340L, 10358L, 10431L, 10439L, 10483L, 10489L, 10494L, 10515L,
        10527L, 10528L, 10545L, 10555L, 10567L, 10585L, 10591L, 10607L, 10625L, 10674L, 10681L, 10693L, 10830L,
        10882L, 10895L, 11036L, 11153L, 11187L, 11224L, 11237L, 11249L, 11324L, 11351L, 11360L, 11544L, 11619L,
        11631L, 11635L, 11688L, 11713L, 11770L, 11774L, 11836L, 11838L, 11932L, 11968L, 11970L, 12153L, 12155L,
        12429L, 12444L, 12445L, 13183L, 13186L, 13313L, 14128L, 14160L, 14324L, 14574L, 14836L, 15512L, 16869L,
        16996L, 17654L, 18239L, 18785L, 18823L, 19404L, 19913L, 19995L, 20352L, 22683L, 23823L, 24021L, 24428L,
        26466L, 26973L, 27066L, 27098L, 27205L, 27449L, 27578L, 28178L, 28468L, 32629L, 32657L, 36630L, 36647L,
        36648L, 37165L, 38055L, 38575L, 38669L, 38700L, 38757L, 39254L, 40108L, 40132L, 43347L, 43593L, 46195L,
        47570L, 47971L, 49013L, 49521L, 49530L, 50014L, 50124L, 50270L, 50619L, 50620L, 51876L, 56386L, 57158L,
        57214L, 57800L, 59053L, 60304L, 61108L, 61791L, 62177L, 62211L, 67395L, 68428L, 68718L, 68721L, 70981L,
        71552L, 71672L, 72105L, 72190L, 73723L, 76338L, 76341L, 76492L, 76600L, 76757L, 77877L, 80321L, 81188L,
        82023L, 82675L, 82690L, 86331L, 86493L, 87101L, 89602L, 93456L, 97020L, 98566L, 99861L, 101299L, 101299L,
        102362L, 102382L, 102651L, 102899L, 105814L, 105836L, 105864L, 105904L, 106646L, 109418L, 109445L,
        109451L, 109689L, 110415L, 112160L, 116236L, 116776L, 118249L, 119450L, 122917L, 124905L, 125509L,
        126889L, 127585L, 131631L, 131634L, 135348L, 138843L, 140300L, 140607L, 141052L, 150540L, 150689L,
        156022L, 157336L, 166424L, 168259L, 172385L, 173129L, 175112L, 177572L, 177677L, 180299L, 181533L,
        181808L, 181812L, 184315L, 184345L, 188927L, 190859L, 194662L, 196867L, 197158L, 198184L, 198663L,
        205584L, 205587L, 205596L, 207703L, 209112L, 210577L, 214756L, 216015L, 223702L, 225886L, 228150L,
        228165L, 232672L, 235040L, 242582L, 246655L, 247136L, 249397L, 249578L, 253935L, 254128L, 257344L,
        259316L, 259693L, 260346L, 260513L, 260514L, 261903L, 262500L, 263115L, 263472L, 265195L, 265208L,
        267935L, 268896L, 269149L, 270303L, 270946L, 271045L, 271110L, 273477L, 273481L, 274855L, 274857L,
        276907L, 277217L, 278154L, 278924L, 278927L, 280180L, 281338L, 283366L, 283995L, 284053L, 284054L,
        290098L, 293660L, 294254L, 296096L, 297270L, 297761L, 297762L, 298094L, 298618L, 299054L, 299534L,
        299536L, 300669L, 312221L, 315162L, 315635L, 316029L, 320288L, 321612L, 323260L, 324552L, 324786L,
        324852L, 324857L, 325358L, 327753L, 329505L, 329865L, 330457L, 331482L, 333167L, 333339L, 335117L,
        335787L, 335797L, 335977L, 335983L, 335984L, 335988L, 336843L, 337167L, 337339L, 337404L, 339846L,
        339964L, 343668L, 345887L, 345938L, 345940L, 346364L, 346698L, 348893L, 351523L, 353081L, 353486L,
        354912L, 355651L, 359410L, 361743L, 362865L, 365177L, 370172L, 370663L, 370964L, 372058L, 372962L,
        381284L, 381288L, 383498L, 383634L, 384018L, 385687L, 387824L, 390845L, 395990L, 396422L, 396535L,
        397243L, 399566L, 399579L, 400155L, 400535L, 410119L, 411088L, 412117L, 414906L, 416494L, 417320L,
        417859L, 419430L, 419743L, 420634L, 420809L, 420817L, 420818L, 421892L, 423108L, 423204L, 424694L,
        424783L, 425909L, 427564L, 427641L, 428078L, 429351L, 429617L, 436270L, 436969L, 437342L, 438148L,
        438631L, 438695L, 439079L, 441282L, 442062L, 444483L, 447277L, 447332L, 447362L, 447365L, 453395L,
        454619L, 454626L, 455476L, 459003L, 459151L, 460458L, 461078L, 462183L, 463461L, 466272L, 466420L,
        467244L, 467407L, 475557L, 476669L, 482600L, 493529L, 496243L, 496450L, 497828L, 502356L, 505292L,
        505642L, 506574L, 507086L, 507089L, 508442L, 508642L, 508883L, 508943L, 508947L, 509967L, 512195L,
        512200L, 514847L, 516486L, 517764L, 519182L, 520758L, 520763L, 521029L, 522162L, 522478L, 522627L,
        522931L, 522938L, 524047L, 524434L, 526007L, 530385L, 533535L, 536554L, 537915L, 537971L, 545611L,
        546554L, 550988L, 553837L, 558144L, 560016L, 565770L, 566525L, 567604L, 568124L, 568620L, 569094L,
        572802L, 573435L, 575264L, 575265L, 577922L, 580489L, 581528L, 585083L, 587727L, 587807L, 588228L,
        592350L, 592695L, 592834L, 593643L, 594767L, 601796L, 602734L, 603692L, 609681L, 613504L, 614930L,
        614933L, 615173L, 615656L, 615777L, 616037L, 619433L, 624091L, 624860L, 626412L, 629542L, 630586L,
        631843L, 634492L, 634649L, 635302L, 635910L, 635996L, 637649L, 639720L, 640146L, 641934L, 646683L,
        653346L, 654739L, 660360L, 663712L, 666277L, 667538L, 675353L, 675445L, 680411L, 682201L, 689160L,
        693134L, 695721L, 697843L, 698343L, 698687L, 698818L, 699172L, 700391L, 700979L, 704673L, 705996L,
        707610L, 709631L, 713704L, 717728L, 718789L, 718821L, 718930L, 719221L, 720321L, 726139L, 726209L,
        729165L, 729791L, 739405L, 739547L, 744275L, 744857L, 746036L, 748783L, 753342L, 758323L, 760104L,
        760774L, 762430L, 762441L, 763215L, 766507L, 774531L, 774825L, 776503L, 784651L, 786892L, 787699L,
        787723L, 791373L, 792307L, 795774L, 799155L, 799379L, 799583L, 807172L, 810693L, 812037L, 812225L,
        818647L, 820232L, 820525L, 821499L, 821937L, 823464L, 826510L, 829402L, 829557L, 829560L, 831815L,
        834562L, 838209L, 840326L, 843416L, 843527L, 845111L, 845783L, 848326L, 848439L, 850888L, 851644L,
        855095L, 856289L, 857553L, 858017L, 859041L, 860867L, 865921L, 866398L, 870404L, 872585L, 872906L,
        882059L, 885303L, 892222L, 893723L, 895959L, 897087L, 899445L, 900667L, 901362L, 906126L, 912649L,
        914206L, 916224L, 920342L, 926393L, 929590L, 930094L, 931405L, 932086L, 932420L, 933090L, 933131L,
        934433L, 934632L, 935271L, 937161L, 937287L, 938614L, 939243L, 940551L, 940721L, 942047L, 943344L,
        944401L, 945675L, 945961L, 948549L, 949423L, 951491L, 955555L, 955916L, 956842L, 959092L, 959098L,
        961323L, 965150L, 966220L, 967582L, 967847L, 968051L, 969492L, 969686L, 974262L, 974558L, 974635L,
        976573L, 976893L, 976906L, 977262L, 978592L, 980083L, 980489L, 982940L, 983507L, 984249L, 984324L,
        985939L, 986843L, 987686L, 996154L, 997086L, 1001311L, 1003596L, 1003598L, 1006540L, 1007826L,
        1008042L, 1008409L, 1010581L, 1010600L, 1011985L, 1012201L, 1014590L, 1015303L, 1016121L, 1016346L,
        1017163L, 1019317L, 1019411L, 1019420L, 1020896L, 1022690L, 1022789L, 1022796L, 1023922L, 1024530L,
        1024621L, 1024773L, 1025463L, 1026436L, 1026563L, 1026819L, 1026999L, 1027535L, 1028703L, 1029529L,
        1029575L, 1029955L, 1030076L, 1031516L, 1032823L, 1034541L, 1038392L, 1039868L, 1040148L, 1041613L,
        1042216L, 1047041L, 1048241L, 1049948L, 1050035L, 1051891L, 1051896L, 1054116L, 1057001L, 1058638L,
        1061990L, 1062807L, 1063879L, 1064178L, 1064375L, 1066262L, 1070535L, 1071215L, 1071866L, 1072342L,
        1072589L, 1072790L, 1075794L, 1076364L, 1078249L, 1079091L, 1079394L, 1079485L, 1083862L, 1084736L,
        1086591L, 1086747L, 1087388L, 1093995L, 1094556L, 1094844L, 1096197L, 1097150L, 1104844L, 1105407L,
        1107028L, 1107387L, 1111873L, 1114738L, 1115395L, 1115623L, 1117006L, 1124127L, 1125311L, 1126357L,
        1127166L, 1128606L, 1129598L, 1130899L, 1131751L, 1134433L, 1136318L, 1139566L, 1139829L, 1140233L,
        1142414L, 1143019L, 1145291L, 1147400L, 1151534L, 1152624L, 1155089L, 1156593L, 1159518L, 1160018L,
        1166073L, 1167366L, 1171462L, 1172648L, 1174618L, 1175038L, 1179558L, 1181511L, 1189205L, 1191610L,
        1192209L, 1197406L, 1197830L, 1200978L, 1207898L, 1209288L, 1209290L, 1211483L, 1212073L, 1214509L,
        1216299L, 1216512L, 1219685L, 1220515L, 1226578L, 1227816L, 1229349L, 1233486L, 1236671L, 1239083L,
        1239146L, 1239251L, 1241674L, 1241748L, 1241982L, 1242372L, 1255350L, 1263421L, 1264966L, 1278099L,
        1279104L, 1280768L, 1284004L, 1284948L, 1290938L, 1291559L, 1294705L, 1302004L, 1305928L, 1308623L,
        1308757L, 1308821L, 1309923L, 1311550L, 1312863L, 1319375L
    );
    public static final int NUMBER_OF_REVIEWS = 2300; // 생성할 리뷰 수
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
        List<ReviewData> reviews = new ArrayList<>();
        generateReviews(NUMBER_OF_REVIEWS, userIds, MOVIE_IDS, reviews, random, startDate,
            secondsBetween);

        // 생성 날짜 기준으로 정렬
        reviews.sort(Comparator.comparing(ReviewData::getCreatedAt));

        // 결과 출력
        StringBuilder sb = new StringBuilder();
        sb.append(
            "INSERT INTO review (user_id, movie_id, title, content, rating, created_at, updated_at) VALUES\n");
        for (int i = 0; i < reviews.size(); i++) {
            ReviewData review = reviews.get(i);
            sb.append(String.format("(%d, %d, '%s', '%s', %.1f, '%s', '%s')",
                review.getUserId(), review.getMovieId(), review.getTitle(), review.getContent(),
                review.getRating(), review.getCreatedAt(), review.getUpdatedAt()));

            if (i < reviews.size() - 1) {
                sb.append(",\n");
            }
        }
        sb.append(";");
        System.out.println(sb.toString());
    }

    private void generateReviews(int numberOfReviews, List<Long> userIds, List<Long> movieIds,
        List<ReviewData> reviews, Random random, LocalDateTime startDate, long secondsBetween) {

        Set<String> existingReviews = new HashSet<>();  // 이미 생성된 리뷰의 userId와 movieId 조합을 저장할 Set

        for (int i = 0; i < numberOfReviews; i++) {
            long userId;
            long movieId;
            String reviewKey;

            // 중복된 리뷰가 생성되지 않도록 검사를 수행
            do {
                userId = getRandomElement(userIds, random);
                movieId = getRandomElement(movieIds, random);
                reviewKey = userId + "_" + movieId;  // userId와 movieId의 조합으로 고유한 키 생성
            } while (existingReviews.contains(reviewKey));  // 중복된 조합이 존재하면 다시 시도

            // 새로운 조합을 Set에 추가
            existingReviews.add(reviewKey);

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

            // 리뷰 데이터를 리스트에 저장
            reviews.add(
                new ReviewData(userId, movieId, title, content, rating, formattedCreationDate,
                    formattedUpdateDate));
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

    // 리뷰 데이터를 담기 위한 내부 클래스
    static class ReviewData {

        private final long userId;
        private final long movieId;
        private final String title;
        private final String content;
        private final float rating;
        private final String createdAt;
        private final String updatedAt;

        public ReviewData(long userId, long movieId, String title, String content, float rating,
            String createdAt, String updatedAt) {
            this.userId = userId;
            this.movieId = movieId;
            this.title = title;
            this.content = content;
            this.rating = rating;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public long getUserId() {
            return userId;
        }

        public long getMovieId() {
            return movieId;
        }

        public String getTitle() {
            return title;
        }

        public String getContent() {
            return content;
        }

        public float getRating() {
            return rating;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }
}