package com.example.simple_blog.domain.token;


import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash(value = "access", timeToLive = 1)
public class Access {

    @Id
    String id;

    @Indexed
    String accessToken;

}


