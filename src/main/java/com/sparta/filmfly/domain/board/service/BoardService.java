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

    @Transactional
    public BoardResponseDto createBoard(BoardRequestDto requestDto, List<MultipartFile> files, User user) {
        user.validateUserStatus(); //탈퇴,정지 상태 비교

        Board entity = requestDto.toEntity(user);
        Board savedBoard = boardRepository.save(entity);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(savedBoard);

        if(files == null || files.isEmpty() || files.get(0).isEmpty()) //파일이 비어있으면 바로 종료
            return boardResponseDto;

        for (MultipartFile file : files) { //파일들 하나씩 s3로 올리기
            MediaResponseDto mediaResponseDto = mediaService.saveMedia(MediaTypeEnum.BOARD,savedBoard.getId(),file);
            boardResponseDto.addMediaDto(mediaResponseDto);
        }

        return boardResponseDto;
    }

    @Transactional(readOnly = true)
    public BoardResponseDto readBoard(Long boardId) {
        Board board = boardRepository.findByIdOrElseThrow(boardId);
        List<Media> mediaList = mediaService.getMediaList(MediaTypeEnum.BOARD,board.getId());

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(board);
        for (Media media : mediaList) {
            boardResponseDto.addMediaDto(MediaResponseDto.fromEntity(media));
        }

        return boardResponseDto;
    }

    @Transactional(readOnly = true)
    public BoardPageResponseDto readBoards(Integer pageNum, Integer size) {
        Pageable pageable = PageRequest.of(pageNum-1, size, Sort.by(Sort.Direction.DESC, "createdAt"));

        Page<Board> boards = boardRepository.findAll(pageable);
        //QueryDSL 최적화 수정
        BoardPageResponseDto boardPageResponseDto = BoardPageResponseDto.fromPage(boards);
        //List 형식 totalPages,size,content,number 등 필요한 정보만 보내는 PageResponse

        List<BoardResponseDto> boardsDto = new ArrayList<>();
        for (Board board : boards) {
            BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(board); //보드 기본 정보
            List<Media> mediaList = mediaService.getMediaList(MediaTypeEnum.BOARD,board.getId());
            //해당 보드의 미디어가 있으면 가지고 온다
            for (Media media : mediaList) {
                boardResponseDto.addMediaDto(MediaResponseDto.fromEntity(media)); //미디어가 존재하면 보드dto에 정보를 넣어준다
            }
            boardsDto.add(boardResponseDto); //보드pageDto content에 넣을 보드 정보 추가
        }

        boardPageResponseDto.addContent(boardsDto);
        return boardPageResponseDto;
    }

    @Transactional
    public BoardResponseDto updateBoard(Long boardId, List<MultipartFile> files, BoardRequestDto requestDto, User user) {
        user.validateUserStatus();
        Board board = boardRepository.findByIdOrElseThrow(boardId); //보드 존재 여부 확인
        board.validateOwner(user); //수정 요청한 유저가 해당 보드의 소유주인지 확인

        board.update(requestDto);
        Board updatedBoard = boardRepository.save(board);

        BoardResponseDto boardResponseDto = BoardResponseDto.fromEntity(updatedBoard);

        //수정 전 기존 미디어들 삭제 요청
        mediaService.deleteAllMedia(MediaTypeEnum.BOARD,board.getId());

        if(files == null || files.isEmpty() || files.get(0).isEmpty()) //파일이 비어있으면 바로 종료
            return boardResponseDto;

        for (MultipartFile file : files) { //파일들 하나씩 s3로 올리기
            MediaResponseDto mediaResponseDto = mediaService.saveMedia(MediaTypeEnum.BOARD,boardId,file);
            boardResponseDto.addMediaDto(mediaResponseDto);
        }

        return boardResponseDto;
    }

    @Transactional
    public String deleteBoard(Long boardId, User user) {
        user.validateUserStatus();

        Board board = boardRepository.findByIdOrElseThrow(boardId);
        if(user.getUserRole() == UserRoleEnum.ROLE_USER) //관리자면 삭제 가능하게
            board.validateOwner(user);

        boardRepository.delete(board);

        return "게시물이 삭제되었습니다.";
    }
}