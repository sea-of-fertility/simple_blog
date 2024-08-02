package com.example.simple_blog.repository.token;

import com.example.simple_blog.domain.token.AccessToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessTokenRepository extends CrudRepository<AccessToken, String> {

    boolean existsByAccessToken(String accessToken);
}
