package com.sparta.filmfly.domain.board.service;

import com.sparta.filmfly.domain.board.dto.BoardPageResponseDto;
import com.sparta.filmfly.domain.board.dto.BoardRequestDto;
import com.sparta.filmfly.domain.board.dto.BoardResponseDto;
import com.sparta.filmfly.domain.board.entity.Board;
import com.sparta.filmfly.domain.board.repository.BoardRepository;
import com.sparta.filmfly.domain.media.dto.MediaResponseDto;
import com.sparta.filmfly.domain.media.entity.Media;
import com.sparta.filmfly.domain.media.entity.MediaTypeEnum;
import com.sparta.filmfly.domain.media.service.MediaService;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.global.util.FileUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final MediaService mediaService;

    /**
     * 보드 생성
     */
    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files, User user) {
        user.validateUserStatus();

        Board entity = requestDto.toEntity(user);
        Board savedBoard = boardRepository.save(entity);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(savedBoard);

        if(!FileUtils.isEmpty(files)) { //파일이 존재하면
            for (MultipartFile file : files) { //파일들 하나씩 s3로 올리기
                MediaResponseDto mediaResponseDto = mediaService.createMedia(MediaTypeEnum.BOARD,savedBoard.getId(),file);
                boardResponseDto.addMediaDto(mediaResponseDto);
            }
        }

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
        for (Media media : mediaList) {
            boardResponseDto.addMediaDto(MediaResponseDto.fromEntity(media));
        }

        return boardResponseDto;
    }

    /**
     * 보드 페이징 조회
     */
    @Transactional(readOnly = true)
    public BoardPageResponseDto getPageBoard(Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        //QueryDSL 최적화로 변경하기
        Page<Board> boards = boardRepository.findAll(pageable);
        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse
        BoardPageResponseDto boardPageResponseDto = BoardPageResponseDto.fromPage(boards);

        List<BoardResponseDto> boardsDto = new ArrayList<>();
        for (Board board : boards) {
            BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(board); //보드 기본 정보 Dto
            List<Media> mediaList = mediaService.getListMedia(MediaTypeEnum.BOARD,board.getId()); //해당 보드의 미디어가 있으면 가지고 온다
            for (Media media : mediaList) {
                boardResponseDto.addMediaDto(MediaResponseDto.fromEntity(media)); //미디어가 존재하면 보드dto에 정보를 넣어준다
            }
            boardsDto.add(boardResponseDto); //보드pageDto content에 넣을 보드 정보 추가
        }

        boardPageResponseDto.addContent(boardsDto);
        return boardPageResponseDto;
    }

    /**
     * 보드 수정
     */
    @Transactional
    public BoardResponseDto updateBoard(User user, BoardRequestDto requestDto, List<MultipartFile> files, Long boardId) {
        user.validateUserStatus();
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        board.validateOwner(user);

        board.update(requestDto);
        Board updatedBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(updatedBoard);

        //수정 전 기존 미디어들 삭제 요청
        mediaService.deleteAllMedia(MediaTypeEnum.BOARD,board.getId());

        if(!FileUtils.isEmpty(files)) {
            for (MultipartFile file : files) {
                MediaResponseDto mediaResponseDto = mediaService.createMedia(MediaTypeEnum.BOARD,boardId,file);
                boardResponseDto.addMediaDto(mediaResponseDto);
            }
        }

        return boardResponseDto;
    }

    /**
     * 보드 삭제
     */
    @Transactional
    public String deleteBoard(User user, Long boardId) {
        user.validateUserStatus();
        Board board = boardRepository.findByIdOrElseThrow(boardId);

        //관리자면 삭제 가능하게
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) {
            board.validateOwner(user);
        }

        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }
}