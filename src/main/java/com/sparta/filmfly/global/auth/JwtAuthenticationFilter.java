package com.sparta.filmfly.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.filmfly.domain.user.dto.UserLoginRequestDto;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.entity.UserRoleEnum;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.global.exception.custom.detail.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

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
        setFilterProcessesUrl("/users/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter: 인증 시도 시작");
        try {
            UserLoginRequestDto requestDto = objectMapper.readValue(request.getInputStream(), UserLoginRequestDto.class);
            User user = userRepository.findByUsernameOrElseThrow(requestDto.getUsername());

            user.validatePassword(requestDto.getPassword(), passwordEncoder);

            if (user.getUserStatus() == UserStatusEnum.SUSPENDED) {
                clearCookies(response); // 쿠키 삭제 추가
                throw new SuspendedException(ResponseCodeEnum.USER_SUSPENDED);
            }

            if (user.getUserStatus() == UserStatusEnum.DELETED) {
                handleTokenGeneration(response, user);
                response.setStatus(ResponseCodeEnum.USER_DELETED.getHttpStatus().value());
                ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.of(ResponseCodeEnum.USER_DELETED.getHttpStatus(), ResponseCodeEnum.USER_DELETED.getMessage());
                writeResponseBody(response, responseEntity);
            }

            List<SimpleGrantedAuthority> authorities = (user.getUserRole() == UserRoleEnum.ROLE_ADMIN)
                    ? List.of(new SimpleGrantedAuthority("ROLE_ADMIN"))
                    : List.of(new SimpleGrantedAuthority("ROLE_USER"));

            Authentication auth = new UsernamePasswordAuthenticationToken(new UserDetailsImpl(user), null, authorities);
            handleTokenGeneration(response, user); // 토큰 생성 및 쿠키 추가
            return auth;
        } catch (NotFoundException e) {
            setCustomErrorResponse(response, ResponseCodeEnum.USER_NOT_FOUND);
            return null;
        } catch (InformationMismatchException e) {
            setCustomErrorResponse(response, ResponseCodeEnum.PASSWORD_INCORRECT);
            return null;
        } catch (SuspendedException e) {
            setCustomErrorResponse(response, ResponseCodeEnum.USER_SUSPENDED);
            return null;
        } catch (IOException e) {
            setErrorResponse(response, ResponseCodeEnum.INVALID_REQUEST);
            return null;
        } catch (Exception e) {
            setErrorResponse(response, ResponseCodeEnum.LOGIN_FAILED);
            return null;
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException {
        log.info("JwtAuthenticationFilter: 인증 성공");
        UserDetailsImpl userDetails = (UserDetailsImpl) authResult.getPrincipal();
        User user = userDetails.getUser();

        response.setStatus(HttpServletResponse.SC_OK);
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.success();
        writeResponseBody(response, responseEntity);
    }

    private void handleTokenGeneration(HttpServletResponse response, User user) {
        String accessToken = jwtProvider.createAccessToken(user.getUsername(), user.getId());
        String refreshToken = jwtProvider.createRefreshToken(user.getUsername(), user.getId());

        ResponseCookie accessCookieBuilder = ResponseCookie.from("accessToken", accessToken)
                .path("/")
                .sameSite("None")
                .secure(true)
                .maxAge(JwtProvider.ACCESS_TOKEN_TIME)
                .build();
        ResponseCookie refreshCookieBuilder = ResponseCookie.from("refreshToken", refreshToken)
                .path("/")
                .sameSite("None")
                .secure(true)
                .maxAge(JwtProvider.REFRESH_TOKEN_TIME)
                .build();

        response.addHeader("Set-Cookie", accessCookieBuilder.toString());
        response.addHeader("Set-Cookie", refreshCookieBuilder.toString());

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        log.info("JwtAuthenticationFilter: 인증 실패");
        setErrorResponse(response, ResponseCodeEnum.LOGIN_FAILED);
    }

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

    private void setErrorResponse(HttpServletResponse response, ResponseCodeEnum responseCode) {
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.of(responseCode.getHttpStatus(), responseCode.getMessage());
        writeErrorResponse(response, responseEntity);
    }

    private void setCustomErrorResponse(HttpServletResponse response, ResponseCodeEnum responseCode) {
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.of(responseCode.getHttpStatus(), responseCode.getMessage());
        writeErrorResponse(response, responseEntity);
    }

    private void writeErrorResponse(HttpServletResponse response, ResponseEntity<MessageResponseDto> responseEntity) {
        try {
            writeResponseBody(response, responseEntity);
        } catch (IOException e) {
            log.error("에러 응답 본문 쓰기 실패: {}", e.getMessage());
        }
    }

    private void clearCookies(HttpServletResponse response) {
        Cookie accessTokenCookie = new Cookie("accessToken", null);
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(0);
        response.addCookie(accessTokenCookie);

        Cookie refreshTokenCookie = new Cookie("refreshToken", null);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(0);
        response.addCookie(refreshTokenCookie);
    }
}
