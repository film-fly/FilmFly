package com.sparta.filmfly.global.config;

import com.sparta.filmfly.domain.user.repository.UserRepository;
import com.sparta.filmfly.global.auth.CustomAccessDeniedHandler;
import com.sparta.filmfly.global.auth.CustomAuthenticationEntryPoint;
import com.sparta.filmfly.global.auth.JwtAuthenticationFilter;
import com.sparta.filmfly.global.auth.JwtAuthorizationFilter;
import com.sparta.filmfly.global.auth.JwtProvider;
import com.sparta.filmfly.global.auth.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final PasswordEncoder passwordEncoder;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(jwtProvider, userRepository, objectMapper, passwordEncoder);
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return filter;
    }

    @Bean
    public JwtAuthorizationFilter jwtAuthorizationFilter() {
        return new JwtAuthorizationFilter(jwtProvider, userDetailsService, userRepository, objectMapper);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        http.authorizeHttpRequests(authorizeHttpRequests ->
                authorizeHttpRequests
//                    .requestMatchers("/", "/error", "/users/signup", "/users/login", "/users/kakao/authorize", "/users/kakao/callback", "/emails/verify", "/emails/code-send","/users/check-nickname").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/admins/**").hasAuthority("ROLE_ADMIN")
//                    .requestMatchers(HttpMethod.PATCH, "/admins/**").hasAuthority("ROLE_ADMIN")
//                    .requestMatchers("/image/upload", "/image/delete", "/temp/**").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/movie/genres/api").permitAll()
//                    .requestMatchers("/test/**").permitAll()
//                    .requestMatchers(HttpMethod.GET, "/movies/**", "/reviews/**", "/boards/**", "/comments/**").permitAll()



                    .requestMatchers("/test/**").permitAll()
                    .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                    .requestMatchers("/emails/**").permitAll()
                    .requestMatchers("/admins/**").hasRole("ADMIN")
                    .requestMatchers("/users/kakao/authorize", "/users/kakao/callback", "/users/check-nickname").permitAll()
                    .requestMatchers(HttpMethod.GET, "/users/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/movies", "/movies/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/genres").permitAll()
                    .requestMatchers(HttpMethod.GET, "/reviews/**", "/movies/*/reviews").permitAll()
                    .requestMatchers("/image/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/boards/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/comments/**", "/boards/*/comments").permitAll()
                    .requestMatchers(HttpMethod.GET, "/officeboards", "/officeboards/*").permitAll()
                    .requestMatchers(HttpMethod.GET, "/goods/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/bads/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/favorites/**").permitAll()
                    .requestMatchers(HttpMethod.GET, "/collections/**").permitAll()
                    .anyRequest().authenticated()
        );

        http.exceptionHandling(exceptionHandling -> exceptionHandling
            .authenticationEntryPoint(customAuthenticationEntryPoint)
            .accessDeniedHandler(customAccessDeniedHandler)
        );

        http.addFilterBefore(jwtAuthorizationFilter(), JwtAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}