package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.*;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.file.service.FileService;
import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.media.service.MediaService;
import com.sparta.filmfly.domain.reaction.ReactionContentTypeEnum;
import com.sparta.filmfly.domain.reaction.service.BadService;
import com.sparta.filmfly.domain.reaction.service.GoodService;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MediaService mediaService;
    private final GoodService goodService;
    private final BadService badService;
    private final UserRepository userRepository;

    private final FileService fileService;
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
        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(updatedBoard);

        return boardResponseDto;
    }

    /**
     * 보드 조회
     */
    @Transactional
    public BoardResponseDto getBoard(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        List<Media> mediaList = mediaService.getListMedia(MediaTypeEnum.BOARD,board.getId());

        board.addHits();
        Board savedBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(savedBoard);
        updateDtoReactionCount(boardResponseDto,savedBoard.getId());

        return boardResponseDto;
    }

    /**
     * 보드 페이징 조회
     */
//    @Transactional(readOnly = true)
//    public BoardPageResponseDto getPageBoard(Integer pageNum, Integer size, Boolean filterGoodCount, Boolean filterHits) {
//        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//
//        //QueryDSL 최적화로 변경하기
//        Page<Board> boards = boardRepository.findAll(pageable);
//        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse
//        BoardPageResponseDto boardPageResponseDto = BoardPageResponseDto.fromPage(boards);
//
//        List<BoardResponseDto> boardsDto = new ArrayList<>();
//        for (Board board : boards) {
//            BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(board); //보드 기본 정보 Dto
//            updateDtoReactionCount(boardResponseDto,board.getId());
//
//            /*
//            List<Media> mediaList = mediaService.getListMedia(MediaTypeEnum.BOARD,board.getId()); //해당 보드의 미디어가 있으면 가지고 온다
//            for (Media media : mediaList) {
//                boardResponseDto.addMediaDto(MediaResponseDto.fromEntity(media)); //미디어가 존재하면 보드dto에 정보를 넣어준다
//            }*/
//            boardsDto.add(boardResponseDto); //보드pageDto content에 넣을 보드 정보 추가
//        }
//
//        boardPageResponseDto.addContent(boardsDto);
//        return boardPageResponseDto;
//    }

    public BoardPageResponseDto getPageBoard(int pageNum, int size, Long filterGoodCount, Long filterHits, String title) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));
//        Page<BoardPageQueryDslDto> boardPage = boardRepository.findAllWithFilters(pageable, filterGoodCount, filterHits, title);
//        return BoardPageResponseDto.fromPage(boardPage);
        return boardRepository.findAllWithFilters(pageable, filterGoodCount, filterHits, title);
    }

    /**
     * 보드 수정 권한 체크
     */
    public Boolean checkEditBoardPermission(User user, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.validateOwner(user);
        return true; //수정 권한 없으면 에러?
    }

    /**
     * 보드 수정 페이지 정보
     */
    public BoardEditResponseDto editBoard(User user, Long boardId) {

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.validateOwner(user);
        return BoardEditResponseDto.fromEntity(board);
    }

    /**
     * 보드 수정
     */
    @Transactional
    public BoardResponseDto updateBoard(User user, BoardRequestDto requestDto, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.validateOwner(user);

        String content = requestDto.getContent();
        fileService.checkModifiedImageFile(MediaTypeEnum.BOARD, board.getId(), content); //이미지 변경 확인
        String modifiedContent = fileService.uploadLocalImageToS3(MediaTypeEnum.BOARD,board.getId(),content); //이미지 S3 변환

        board.updateContent(requestDto.getTitle(),modifiedContent);
        Board updatedBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(updatedBoard);
        updateDtoReactionCount(boardResponseDto,updatedBoard.getId());

        return boardResponseDto;
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public String deleteBoard(User user, Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        //관리자면 삭제 가능하게
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) {
            board.validateOwner(user);
        }

        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }

    /**
     * Dto에 goodCount, badCount 업데이트하는 함수
     */
    private void updateDtoReactionCount(BoardResponseDto boardResponseDto, Long boardId){
        Long goodCount = goodService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
        Long badCount = badService.getCountByTypeTypeId(ReactionContentTypeEnum.BOARD,boardId);
        boardResponseDto.updateReactionCount(goodCount,badCount);
    }

    public BoardPageResponseDto getUsersBoard(Integer pageNum, Integer size, Long userId) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        return boardRepository.findAllByUserId(userId,pageable);
    }
}