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

    private final List<String> anyMethodWhiteList = List.of(
            "/",
            "/error",
            "/users/signup",
            "/users/login",
            "/users/kakao/authorize",
            "/users/kakao/callback",
            "/emails/verify",
            "/emails/code-send",
            "/users/check-nickname"
            "/emails/[0-9]+/resend",
            "/users/check-nickname",
            "/movie/genres/api"
    );

    private final List<String> getMethodWhiteList = List.of(
            "/users/[0-9]+"
    );

    private final List<String> deletedUserAllowedPaths = List.of(
            "/users/logout", "/users/activate"
    );

    private final List<String> unverifiedUserAllowedPaths = List.of(
            "/users/logout"
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

        // 인증 불필요
        if (isWhiteListed(uri) || isGetMethodWhiteListed(req.getMethod(), uri)) {
            log.info("인증이 필요 없는 요청: {}", uri);
            filterChain.doFilter(req, res);
            return;
        }

        try {
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

            if (user.getUserStatus() == UserStatusEnum.UNVERIFIED) {
                if (isUnverifiedUserAllowedPath(uri)) {
                    handleValidAccessToken(accessToken, user);
                    filterChain.doFilter(req, res);
                    return;
                } else {
                    setErrorResponse(res, ResponseCodeEnum.EMAIL_VERIFICATION_REQUIRED);
                    return;
                }
            }

            if (user.getUserStatus() == UserStatusEnum.DELETED) {
                if (isDeletedUserAllowedPath(uri)) {
                    handleValidAccessToken(accessToken, user);
                    filterChain.doFilter(req, res);
                    return;
                } else {
                    setErrorResponse(res, ResponseCodeEnum.USER_DELETED);
                    return;
                }
            } else if (user.getUserStatus() == UserStatusEnum.SUSPENDED) {
                setErrorResponse(res, ResponseCodeEnum.USER_SUSPENDED);
                return;
            }

            boolean accessTokenValid = jwtProvider.validateToken(accessToken);
            if (accessTokenValid) {
                log.info("유효한 액세스 토큰 처리");
                handleValidAccessToken(accessToken, user);
            } else {
                log.info("액세스 토큰 만료, 리프레시 토큰으로 재발급");
                handleExpiredAccessToken(req, res, user);
                return;
            }
        } catch (ExpiredJwtException e) {
            log.info("만료된 액세스 토큰 처리");
            handleExpiredAccessToken(req, res, null);
            return;
        } catch (JwtException | IllegalArgumentException | AuthenticationServiceException e) {
            setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
            return;
        }

        filterChain.doFilter(req, res);
    }

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

    private void handleValidAccessToken(String accessToken, User user) {
        Claims accessTokenClaims = jwtProvider.getUserInfoFromToken(accessToken);
        String username = accessTokenClaims.getSubject();
        setAuthentication(username, user.getId());
    }

    private void handleExpiredAccessToken(HttpServletRequest req, HttpServletResponse res, User user) {
        String refreshToken = getTokenFromCookie(req, "refreshToken");
        if (StringUtils.hasText(refreshToken) && jwtProvider.validateToken(refreshToken)) {
            Claims refreshTokenClaims = jwtProvider.getUserInfoFromToken(refreshToken);
            String username = refreshTokenClaims.getSubject();

            if (user == null) {
                user = userRepository.findByUsername(username).orElse(null);
            }

            if (user != null && refreshToken.equals(user.getRefreshToken())) {
                if (user.getUserStatus() == UserStatusEnum.DELETED) {
                    if (isDeletedUserAllowedPath(req.getRequestURI())) {
                        String newAccessToken = jwtProvider.createAccessToken(username, user.getId());
                        res.addHeader(JwtProvider.AUTHORIZATION_HEADER, newAccessToken);
                        setAuthentication(username, user.getId());
                        return;
                    } else {
                        setErrorResponse(res, ResponseCodeEnum.USER_DELETED);
                        return;
                    }
                } else if (user.getUserStatus() == UserStatusEnum.SUSPENDED) {
                    setErrorResponse(res, ResponseCodeEnum.USER_SUSPENDED);
                    return;
                }

                String newAccessToken = jwtProvider.createAccessToken(username, user.getId());
                res.addHeader(JwtProvider.AUTHORIZATION_HEADER, newAccessToken);
                setAuthentication(username, user.getId());
            } else {
                setErrorResponse(res, ResponseCodeEnum.INVALID_REQUEST);
            }
        } else {
            setErrorResponse(res, ResponseCodeEnum.INVALID_TOKENS);
        }
    }

    public void setAuthentication(String username, Long userId) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username, userId);
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
    }

    private Authentication createAuthentication(String username, Long userId) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        // 사용자 ID를 Principal로 설정
        authToken.setDetails(userId);
        return authToken;
    }

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

    private void setErrorResponse(HttpServletResponse response, ResponseCodeEnum responseCode) {
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

    private boolean isWhiteListed(String uri) {
        return anyMethodWhiteList.stream().anyMatch(pattern -> Pattern.matches(pattern, uri));
    }

    private boolean isGetMethodWhiteListed(String method, String uri) {
        return HttpMethod.GET.matches(method) && getMethodWhiteList.stream().anyMatch(pattern -> Pattern.compile(pattern).matcher(uri).matches());
    }

    private boolean isUnverifiedUserAllowedPath(String uri) {
        return unverifiedUserAllowedPaths.stream().anyMatch(uri::startsWith);
    }

    private boolean isDeletedUserAllowedPath(String uri) {
        return deletedUserAllowedPaths.stream().anyMatch(uri::startsWith);
    }
}
