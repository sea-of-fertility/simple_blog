package com.example.simple_blog.service.token;


import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.domain.token.AccessToken;
import com.example.simple_blog.exception.token.AccessTokenInvalidException;
import com.example.simple_blog.repository.AccessTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final AccessTokenRepository accessTokenRepository;
    private final TokenProperties tokenProperties;

    public void setBlackList(String accessToken) {
        AccessToken accesstoken = AccessToken.builder()
                .accessToken(accessToken)
                .expiresIn(tokenProperties.getAccessTokenExpirationMinutes())
                .build();
        accessTokenRepository.save(accesstoken);
    }

    public Boolean isValidAccessToken(String accessToken) {

        // blacklist에 등록이 됐다는 의미이므로 예외를 발생시킨다.
        if (accessTokenRepository.existsByAccessToken(accessToken)) {
            throw new AccessTokenInvalidException("로그아웃된 access token 입니다.");
        }
        // black list에 등록되어 있지 않다.
        return true;
    }


}

