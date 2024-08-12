package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.*;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.file.service.FileService;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.dto.ReactionCheckResponseDto;
import com.sparta.filmfly.domain.reaction.service.BadService;
import com.sparta.filmfly.domain.reaction.service.GoodService;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.global.auth.UserDetailsImpl;
import com.sparta.filmfly.global.common.response.PageResponseDto;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private static final String BOARD_HITS_PREFIX = "board:hits:";

    private final BoardRepository boardRepository;
    private final GoodService goodService;
    private final BadService badService;
    private final FileService fileService;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * 보드 생성
     */
    @Transactional
    public BoardResponseDto createBoard(User user, BoardRequestDto requestDto) {
        Board entity = requestDto.toEntity(user);
        Board savedBoard = boardRepository.save(entity); // 일단 저장 후 boardId를 생성

        //이미지 올릴때 검사, 이미지 전부 합쳐서 20MB 못넘게 하기 필요할까??
        String content = requestDto.getContent();
        String modifiedContent = fileService.uploadLocalImageToS3(MediaTypeEnum.BOARD,savedBoard.getId(), content);

        savedBoard.updateContent(null, modifiedContent);
        Board updatedBoard = boardRepository.save(savedBoard);

        return BoardResponseDto.fromEntity(updatedBoard,0L,0L);
    }

    /**
     * 보드 조회
     */
    @Transactional(readOnly = true)
    public BoardReactionResponseDto getBoard(UserDetailsImpl userDetails, Long boardId) {
        Board findBoard = boardRepository.findByIdOrElseThrow(boardId);

        Long hits = plusBoardHits(findBoard);

        BoardResponseDto boardDto = boardRepository.getBoard(boardId);
        boardDto.updateHits(hits);
        ReactionCheckResponseDto reactions = ReactionCheckResponseDto.setupFalse();
        if (userDetails != null) {
            reactions = boardRepository.checkBoardReaction(userDetails.getUser(), boardId);
        }

        return BoardReactionResponseDto.of(boardDto, reactions);

//        Board board = boardRepository.findByIdOrElseThrow(boardId);
//
//        board.addHits();
//        Board savedBoard = boardRepository.save(board);
//
//        Long goodCount = goodService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
//        Long badCount = badService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
//        return BoardResponseDto.fromEntity(savedBoard, goodCount, badCount);
    }

    /**
     * 보드 페이징 조회
     */
    public PageResponseDto<List<BoardPageDto>> getPageBoard(Long filterGoodCount, Long filterHits, String search, Pageable pageable) {
        return boardRepository.findAllWithFilters(pageable, filterGoodCount, filterHits, search);
    }

    /**
     * 유저의 보드 조회
     */
    public PageResponseDto<List<BoardPageDto>> getUsersBoard(Long userId, Pageable pageable) {
        return boardRepository.findAllByUserId(userId,pageable);
    }

    /**
     * 보드 수정 권한 체크
     */
    public Boolean getBoardUpdatePermission(User user, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        //admin이면 true 반환
        if(!user.isAdmin()) {
            board.checkOwnerUser(user);
        }
        return true; //수정 권한 없으면 에러?
    }

    /**
     * 보드 수정 페이지 정보
     */
    public BoardUpdateResponseDto forUpdateBoard(User user, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkOwnerUser(user);
        return BoardUpdateResponseDto.fromEntity(board);
    }

    /**
     * 보드 수정
     */
    @Transactional
    public BoardResponseDto updateBoard(User user, BoardRequestDto requestDto, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.checkOwnerUser(user);

        String content = requestDto.getContent();
        fileService.checkModifiedImageFile(MediaTypeEnum.BOARD, board.getId(), content); //이미지 변경 확인
        String modifiedContent = fileService.uploadLocalImageToS3(MediaTypeEnum.BOARD,board.getId(),content); //이미지 S3 변환

        board.updateContent(requestDto.getTitle(),modifiedContent);
        Board updatedBoard = boardRepository.save(board);

        Long goodCount = goodService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
        Long badCount = badService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
        return BoardResponseDto.fromEntity(updatedBoard,goodCount,badCount);
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public String deleteBoard(User user, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        //관리자면 삭제 가능하게
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) {
            board.checkOwnerUser(user);
        }

        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }

    /**
     * 보드 수 반환
     */
    public long getBoardCount() {
        return boardRepository.count();
    }

    /**
     * redis로 해당 게시물 조회수 +1 (redis)
     */
    private Long plusBoardHits(Board board) {
        String key = BOARD_HITS_PREFIX + board.getId();

        // Redis에서 조회수 가져오기
        String redisHits = redisTemplate.opsForValue().get(key);
        Long currentHits;

        // 레디스가 빈값이면 DB의 조회수 가져오기
        if (!StringUtils.hasText(redisHits)) {
            currentHits = board.getHits();
            redisTemplate.opsForValue().set(key, currentHits.toString(), Duration.ofHours(25)); // 25시간 TTL 설정
        }

        // 조회수 1 증가
        Long updatedHits = redisTemplate.opsForValue().increment(key);

        redisTemplate.expire(key, Duration.ofHours(25)); // 매번 조회 시 TTL 연장

        return updatedHits;
    }


    /**
     * 해당 게시물 조회수 가져오기 (redis)
     */
    private Long getBoardHits(Long boardId) {
        String key = BOARD_HITS_PREFIX + boardId;
        String viewCountStr = redisTemplate.opsForValue().get(key);
        return viewCountStr != null ? Long.parseLong(viewCountStr) : 0L;
    }
}