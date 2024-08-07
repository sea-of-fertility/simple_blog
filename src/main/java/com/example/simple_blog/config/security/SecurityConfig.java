package com.example.simple_blog.config.security;

import com.example.simple_blog.config.filter.CustomLogoutFilter;
import com.example.simple_blog.config.filter.LoginFilter;
import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.security.jwt.JwtFilter;
import com.example.simple_blog.security.jwt.JwtUtil;
import com.example.simple_blog.service.token.AccessTokenService;
import com.example.simple_blog.service.token.RefreshTokenService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final ObjectMapper objectMapper;
    private final JwtUtil jwtUtil;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final RefreshTokenService refreshTokenService;
    private final TokenProperties tokenProperties;
    private final AccessTokenService accessTokenService;

    @Value("${login.url}")
    private String defaultUrl;

    @Bean
    public PasswordEncoder passwordencoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        http
                .sessionManagement((auth) -> auth.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.
                securityMatcher("/**")
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/chat-blog/public/**").permitAll()
                        .requestMatchers("/chat-blog/user/**").hasRole("USER")
                        .requestMatchers("/chat-blog/admin").hasRole("ADMIN")
                        .anyRequest().denyAll());

        http
                .addFilterAt(LoginFilter.builder()
                        .defaultFilterProcessesUrl(defaultUrl)
                        .objectMapper(objectMapper)
                        .jwtUtil(jwtUtil)
                        .refreshTokenService(refreshTokenService)
                        .tokenProperties(tokenProperties)
                        .build()
                        , UsernamePasswordAuthenticationFilter.class);

        http
                .addFilterBefore(JwtFilter.builder()
                        .jwtUtil(jwtUtil)
                        .accessTokenService(accessTokenService)
                        .build(), LoginFilter.class);

        http
                .addFilterBefore(CustomLogoutFilter.builder()
                        .jwtUtil(jwtUtil)
                        .accessTokenService(accessTokenService)
                        .refreshTokenService(refreshTokenService)
                        .build(), LogoutFilter.class);

        http
                .csrf(AbstractHttpConfigurer::disable);

        http
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin((AbstractHttpConfigurer::disable));

        return http.build();
    }

}
