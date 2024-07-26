package com.sparta.filmfly.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.filmfly.domain.user.dto.UserLoginRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j(topic = "로그인 처리 및 JWT 생성")
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;

    public JwtAuthenticationFilter(JwtProvider jwtProvider, UserRepository userRepository, ObjectMapper objectMapper, PasswordEncoder passwordEncoder) {
        this.jwtProvider = jwtProvider;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
        this.passwordEncoder = passwordEncoder;
        // 로그인 URL 설정
        setFilterProcessesUrl("/users/login");
    }

    /**
     * 인증 시도
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter: 인증 시도 시작");
        try {
            // 요청 본문에서 로그인 요청 DTO 읽기
            UserLoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);

            // 로그인 시도하는 username 조회
            User user = userRepository.findByUsernameOrElseThrow(requestDto.getUsername());

            // 사용자 상태(탈퇴, 정지, 인증) 검증
            user.validateUserStatus();

            // 비밀번호 검증
            user.validatePassword(requestDto.getPassword(), passwordEncoder);

            // 인증 토큰 생성 및 반환
            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(
                            requestDto.getUsername(),
                            requestDto.getPassword(),
                            null
                    )
            );
        } catch (IOException e) {
            // 요청 본문 읽기 실패 시 에러 응답 설정
            setErrorResponse(response, ResponseCodeEnum.INVALID_TOKENS);
            return null;
        }
    }

    /**
     * 인증 성공 시 처리
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("JwtAuthenticationFilter: 인증 성공");
        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();

        // 액세스 토큰 및 리프레시 토큰 생성
        String accessToken = jwtProvider.createAccessToken(username);
        String refreshToken = jwtProvider.createRefreshToken(username);

        Cookie accessTokenCookie = new Cookie("accessToken", accessToken);
        accessTokenCookie.setPath("/");
        response.addCookie(accessTokenCookie);
        Cookie refreshTokenCookie = new Cookie("refreshToken", refreshToken);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);

        // 사용자 정보 업데이트
        User user = ((UserDetailsImpl) authResult.getPrincipal()).getUser();
        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        // 성공 응답 설정
        response.setStatus(HttpServletResponse.SC_OK);
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.success();
        writeResponseBody(response, responseEntity);
    }

    /**
     * 인증 실패 시 처리
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("JwtAuthenticationFilter: 인증 실패");
        // 로그인 실패 시 에러 응답 설정
        setErrorResponse(response, ResponseCodeEnum.LOGIN_FAILED);
    }

    /**
     * 응답 본문 작성
     */
    private void writeResponseBody(HttpServletResponse response, ResponseEntity<MessageResponseDto> responseEntity) throws IOException {
        response.setStatus(responseEntity.getStatusCode().value());
        response.setContentType("application/json");
        try (ServletOutputStream outputStream = response.getOutputStream()) {
            objectMapper.writeValue(outputStream, responseEntity.getBody());
            outputStream.flush();
        } catch (IOException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.setContentType("application/json");
            response.getWriter().write("{\"statusCode\": 500, \"message\": \"Internal Server Error\"}");
        }
    }

    /**
     * 에러 응답 설정
     */
    private void setErrorResponse(HttpServletResponse response, ResponseCodeEnum responseCode) {
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.of(responseCode.getHttpStatus(), responseCode.getMessage());
        try {
            writeResponseBody(response, responseEntity);
        } catch (IOException e) {
            log.error("에러 응답 본문 쓰기 실패: {}", e.getMessage());
        }
    }
}
