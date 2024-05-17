package com.example.simple_blog.service.token;


import com.example.simple_blog.config.properties.TokenProperties;
import com.example.simple_blog.exception.token.AccessTokenInvalidException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AccessTokenService {

    private final StringRedisTemplate stringRedisTemplate;
    private final TokenProperties tokenProperties;

    public void setBlackList(String access, String address) {
        stringRedisTemplate.opsForValue().set(access, address, tokenProperties.getAccessTokenExpirationMinutes(), TimeUnit.MINUTES);
    }

    public Boolean isValidAccessToken(String accessToken) {
        if(Boolean.TRUE.equals(stringRedisTemplate.hasKey(accessToken))){
            throw new AccessTokenInvalidException("로그아웃된 access token 입니다.");
        }
        return true;
    }


}

