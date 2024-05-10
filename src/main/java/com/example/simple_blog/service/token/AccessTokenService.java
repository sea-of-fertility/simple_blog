package com.example.simple_blog.service.token;


import com.example.simple_blog.domain.token.Access;
import com.example.simple_blog.exception.token.AccessTokenInvalidException;
import com.example.simple_blog.repository.AccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccessTokenService {


    private final Boolean valid = true;

    private final AccessRepository accessRepository;


    public void setBlackList(String access) {
        Access build = Access.builder()
                .accessToken(access)
                .build();
        accessRepository.save(build);
    }


    public Boolean existByAccessToken(String accessToken) {
        return accessRepository.existsByAccessToken(accessToken);
    }


    public Boolean existBlackList(String accessToken) {
        if (!this.existByAccessToken(accessToken)) {
            return this.valid;
        }
        throw new AccessTokenInvalidException("로그아웃된 accessToken 입니다.");
    }
}
