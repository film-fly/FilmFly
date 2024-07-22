package com.sparta.filmfly.domain.officeboard.service;

import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardRequestDto;
import com.sparta.filmfly.domain.officeboard.dto.OfficeBoardResponseDto;
import com.sparta.filmfly.domain.officeboard.entity.OfficeBoard;
import com.sparta.filmfly.domain.officeboard.repository.OfficeBoardRepository;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.exception.custom.detail.NotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
// 공통 -> 로그인 완료되면 유저 추가
public class OfficeBoardService {

    private final OfficeBoardRepository officeBoardRepository;
    private final UserRepository userRepository;


    @Transactional
    public OfficeBoardResponseDto createOfficeBoard(User user, OfficeBoardRequestDto requestDto) {
        OfficeBoard officeBoard = OfficeBoard.builder()
                .user(user)
                .requestDto(requestDto)
                .build();

        officeBoard.validUser();

        officeBoardRepository.save(officeBoard);
        return OfficeBoardResponseDto.fromEntity(officeBoard);
    }

    @Transactional
    public List<OfficeBoardResponseDto> findAll(Pageable pageable) {

        List<OfficeBoardResponseDto> list = officeBoardRepository.findAll(pageable)
                .stream()
                .map(OfficeBoardResponseDto::fromEntity)
                .toList();

        return list;
    }

    @Transactional
    public OfficeBoardResponseDto findOne(Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND)
        );

        return OfficeBoardResponseDto.fromEntity(officeBoard);

    }

    @Transactional
    public OfficeBoardResponseDto update(User user, Long id, OfficeBoardRequestDto requestDto) {

        OfficeBoard officeBoard = officeBoardRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND)
        );

        officeBoard.validUser();
        officeBoard.checkAdminUser(user);
        officeBoard.update(requestDto);

        return OfficeBoardResponseDto.fromEntity(officeBoard);

    }


    @Transactional
    public OfficeBoardResponseDto delete(User user, Long id) {

        OfficeBoard officeBoard = officeBoardRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ResponseCodeEnum.BOARD_NOT_FOUND)
        );

        officeBoard.validUser();
        officeBoard.checkAdminUser(user);
        officeBoard.delete();
        return OfficeBoardResponseDto.fromEntity(officeBoard);
    }

}