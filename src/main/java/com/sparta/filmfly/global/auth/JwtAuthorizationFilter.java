package com.sparta.filmfly.global.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.filmfly.domain.user.entity.UserStatusEnum;
import com.sparta.filmfly.global.common.response.MessageResponseDto;
import com.sparta.filmfly.global.common.response.ResponseCodeEnum;
import com.sparta.filmfly.global.common.response.ResponseUtils;
import com.sparta.filmfly.domain.user.entity.User;
import com.sparta.filmfly.domain.user.repository.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    // 화이트 리스트에 포함된 URI 패턴은 인증을 생략
    private final List<String> anyMethodWhiteList = List.of(
            "/", "/error", "/users/signup", "/users/login", "/users/kakao/authorize", "/users/kakao/callback", "/email/verify", "/email/[0-9]+/resend"
    );

   // GET 메서드 화이트 리스트
    private final List<String> getMethodWhiteList = List.of(
           "/users/[0-9]+/profile"
   );

    public JwtAuthorizationFilter(JwtProvider jwtProvider, UserDetailsServiceImpl userDetailsService,
                                  UserRepository userRepository, ObjectMapper objectMapper) {
        this.jwtProvider = jwtProvider;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest req, @NonNull HttpServletResponse res,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String uri = req.getRequestURI();
        log.info("요청된 URI: {}", uri);

        // 화이트 리스트에 포함된 URI는 인증을 생략
        if (isWhiteListed(uri) || isGetMethodWhiteListed(req.getMethod(), uri)) {
            log.info("인증이 필요 없는 요청: {}", uri);
            filterChain.doFilter(req, res);
            return;
        }

        try {
            // 쿠키에서 액세스 토큰과 리프레시 토큰 추출
            String accessToken = getTokenFromCookie(req, "accessToken");
            if (!StringUtils.hasText(accessToken)) {
                setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
                return;
            }

            Claims accessTokenClaims = jwtProvider.getUserInfoFromToken(accessToken);
            String username = accessTokenClaims.getSubject();
            User user = userRepository.findByUsername(username).orElse(null);

            if (user == null || user.getRefreshToken() == null) {
                setErrorResponse(res, ResponseCodeEnum.USER_NOT_FOUND);
                return;
            }

            // 사용자 상태 확인
            if (user.getUserStatus() == UserStatusEnum.DELETED) {
                setErrorResponse(res, ResponseCodeEnum.USER_DELETED);
                return;
            } else if (user.getUserStatus() == UserStatusEnum.SUSPENDED) {
                setErrorResponse(res, ResponseCodeEnum.USER_SUSPENDED);
                return;
            }

            // 액세스 토큰 검증
            boolean accessTokenValid = jwtProvider.validateToken(accessToken);
            if (accessTokenValid) {
                log.info("유효한 액세스 토큰 처리");
                handleValidAccessToken(accessToken);
            } else {
                log.info("액세스 토큰 만료, 리프레시 토큰으로 재발급");
                handleExpiredAccessToken(req, res);
                return;
            }
        } catch (ExpiredJwtException e) {
            log.info("만료된 액세스 토큰 처리");
            handleExpiredAccessToken(req, res);
            return;
        } catch (JwtException | IllegalArgumentException | AuthenticationServiceException e) {
            setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
            return;
        }

        filterChain.doFilter(req, res);
    }

    // 쿠키에서 토큰 추출
    private String getTokenFromCookie(HttpServletRequest request, String tokenName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (tokenName.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // 유효한 액세스 토큰을 처리하여 인증을 설정
    private void handleValidAccessToken(String accessToken) {
        Claims accessTokenClaims = jwtProvider.getUserInfoFromToken(accessToken);
        String username = accessTokenClaims.getSubject();
        setAuthentication(username);
    }

    // 액세스 토큰이 만료된 경우 리프레시 토큰을 통해 새로운 액세스 토큰을 발급
    private void handleExpiredAccessToken(HttpServletRequest req, HttpServletResponse res) {
        // 쿠키에서 리프레시 토큰 추출
        String refreshToken = getTokenFromCookie(req, "refreshToken");
        if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(refreshToken)) {
            Claims refreshTokenClaims = jwtProvider.getUserInfoFromToken(refreshToken);
            String username = refreshTokenClaims.getSubject();

            User user = userRepository.findByUsername(username).orElse(null);
            if (user != null && refreshToken.equals(user.getRefreshToken())) {
                // 사용자 상태 확인
                if (user.getUserStatus() == UserStatusEnum.DELETED) {
                    setErrorResponse(res, ResponseCodeEnum.USER_DELETED);
                    return;
                } else if (user.getUserStatus() == UserStatusEnum.SUSPENDED) {
                    setErrorResponse(res, ResponseCodeEnum.USER_SUSPENDED);
                    return;
                }

                // 새로운 액세스 토큰 생성
                String newAccessToken = jwtProvider.createAccessToken(username);
                res.addHeader(JwtProvider.AUTHORIZATION_HEADER, newAccessToken);
                setAuthentication(username);
            } else {
                setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
            }
        } else {
            setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
        }
    }

    // SecurityContext에 인증 객체를 설정
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    // Username을 통해 Authentication 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // HTTP 응답 본문 작성
    private void writeResponseBody(HttpServletResponse res, ResponseEntity<MessageResponseDto> responseEntity) throws IOException {
        res.setStatus(responseEntity.getStatusCode().value());
        res.setContentType("application/json");
        try (ServletOutputStream outputStream = res.getOutputStream()) {
            objectMapper.writeValue(outputStream, responseEntity.getBody());
            outputStream.flush();
        } catch (IOException e) {
            res.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            res.setContentType("application/json");
            res.getWriter().write("{\"statusCode\": 500, \"message\": \"Internal Server Error\"}");
        }
    }

    // 에러 응답 설정
    private void setErrorResponse(HttpServletResponse response, ResponseCodeEnum responseCode) {
        ResponseEntity<MessageResponseDto> responseEntity = ResponseUtils.of(responseCode.getHttpStatus(), responseCode.getMessage());
        writeErrorResponse(response, responseEntity);
    }

    // 에러 응답 본문 작성
    private void writeErrorResponse(HttpServletResponse response, ResponseEntity<MessageResponseDto> responseEntity) {
        try {
            writeResponseBody(response, responseEntity);
        } catch (IOException e) {
            log.error("에러 응답 본문 쓰기 실패: {}", e.getMessage());
        }
    }

    // 화이트 리스트 검사
    private boolean isWhiteListed(String uri) {
        return anyMethodWhiteList.stream().anyMatch(pattern -> Pattern.matches(pattern, uri));
    }
    // GET 메서드 화이트 리스트 검사
    private boolean isGetMethodWhiteListed(String method, String uri) {
        return HttpMethod.GET.matches(method) && getMethodWhiteList.stream().anyMatch(pattern -> Pattern.compile(pattern).matcher(uri).matches());
    }
}
