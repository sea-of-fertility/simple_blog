package com.example.simple_blog.domain.token;


import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

@Getter
@RedisHash
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AccessToken {

    @Id
    private String id;

    @Indexed
    private String accessToken;

    @TimeToLive
    private Long expiresIn;

    @Builder
    public AccessToken(String accessToken, Long expiresIn) {
        this.accessToken = accessToken;
        this.expiresIn = expiresIn;
    }
}
