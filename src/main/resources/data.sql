INSERT INTO user (created_at, updated_at, email, introduce, kakao_id, nickname, password,
                  picture_url, refresh_token, user_role, user_status, username)
VALUES (now(), now(), 'email@gmail.com', '안녕', 12345, '닉네임', '$2a$12$u48yj1bNay/ordPp//pX9OVd7DdFaNQfbvesn0ME6PAVfl/dicjdC',
        '사진URL', 'refresh', 'ROLE_USER', 'VERIFIED', 'testId1');

INSERT INTO movie (content, rating, summary, title)
VALUES ('영화 내용', 3.3, '요약', '제목');

INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용1', '제목1');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용2', '제목2');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용3', '제목3');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용4', '제목4');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용5', '제목5');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용6', '제목6');
INSERT INTO review (created_at, updated_at, rating, bad_count, good_count, movie_id, user_id, content, title)
VALUES (now(), now(), 4.5, 12, 11, 1, 1, '내용7', '제목7');