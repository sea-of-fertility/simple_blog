package com.example.simple_blog.service.token;

import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.exception.token.TokenException;
import com.example.simple_blog.security.jwt.JwtUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

import static com.example.simple_blog.config.properties.TokenProperties.ACCESS_TOKEN_NAME;


@SpringBootTest
class AccessTokenServiceTest {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    TokenProperties tokenProperties;

    String address = "hello@gmail.com";

    String role = "ROLE_USER";

    @Autowired
    AccessTokenService accessTokenService;


    @Test
    @DisplayName("유효 시간이 지난 access token 삭제")
    void AccessTokenTTL() throws Exception {
        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());

        String address1 = jwtUtil.getAddress(access);
        stringRedisTemplate.opsForValue().set(access, address1, 1L, TimeUnit.SECONDS);
        Thread.sleep(1000);

        Boolean validAccessToken = accessTokenService.isValidAccessToken(access);
        Assertions.assertThat(validAccessToken).isEqualTo(true);
    }

    @Test
    @DisplayName("만기된 access token으로 로그인하면 예외를 발생")
    void blackList() throws Exception {

        //given
        String access = jwtUtil.createJwt(ACCESS_TOKEN_NAME, address, role,
                tokenProperties.getAccessTokenExpirationMinutes());

        String address1 = jwtUtil.getAddress(access);
        accessTokenService.setBlackList(access, address1);

        //then
        Assertions.assertThatThrownBy(() -> accessTokenService.isValidAccessToken(access)).isInstanceOf(TokenException.class);

    }
}