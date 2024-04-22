package com.example.simple_blog.config.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class TokenProperties {

    private final Long accessTokenExpirationMinutes;
    private final Long refreshTokenExpirationDays;
    public static final String ACCESS_TOKEN_NAME = "access";
    public static final String REFRESH_TOKEN_NAME = "refresh";



    public TokenProperties(@Value("${security.token.access.expirationMinutes}")Long accessTokenExpirationMinutes,
                           @Value("${security.token.refresh.expirationDays}") Long refreshTokenExpirationDays)
    {
        this.accessTokenExpirationMinutes = accessTokenExpirationMinutes * 60000;
        this.refreshTokenExpirationDays = refreshTokenExpirationDays * 86400000;
    }
}
