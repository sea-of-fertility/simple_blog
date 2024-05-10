package com.example.simple_blog.service.token;

import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.domain.token.Access;
import com.example.simple_blog.exception.token.TokenException;
import com.example.simple_blog.repository.AccessRepository;
import com.example.simple_blog.security.jwt.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;
import java.time.Instant;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;


@SpringBootTest
class AccessTokenServiceTest {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    TokenProperties tokenProperties;

    String address = "hello@gmail.com";

    String role = "ROLE_USER";

    @Autowired
    AccessTokenService accessTokenService;

    @Autowired
    AccessRepository accessRepository;

    @BeforeEach
    void setAccessRepository() {
        accessRepository.deleteAll();

    }

    @Test
    @DisplayName("redis에 accessToken 저장")
    void setBlackList() throws Exception {

        //given
        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());

        accessTokenService.setBlackList(access);

        //when
        Boolean b = accessRepository.existsByAccessToken(access);

        //then
        Assertions.assertThat(true).isEqualTo(b);

    }

    @Test
    @DisplayName("유효 시간이 지난 access token 삭제")
    void AccessTokenTTL() throws Exception {
        //given
        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());

        accessTokenService.setBlackList(access);
        Clock fixedClock = Clock.fixed(Instant.now().plusSeconds(300), Clock.systemUTC().getZone());

        //when
        Boolean b = accessRepository.existsByAccessToken(access);
        System.out.println(fixedClock);
        //then
        Assertions.assertThat(false).isEqualTo(b);
    }

    @Test
    @DisplayName("만기된 access token으로 로그인하면 예외를 발생")
    void blackList() throws Exception {

        //given
        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());
        Access build = Access.builder()
                .accessToken(access)
                .build();

        accessTokenService.setBlackList(access);

        //then
        Assertions.assertThatThrownBy(() -> accessTokenService.existBlackList(access)).isInstanceOf(TokenException.class);

    }
}