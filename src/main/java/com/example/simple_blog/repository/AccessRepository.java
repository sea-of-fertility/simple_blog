package com.example.simple_blog.repository;

import com.example.simple_blog.domain.token.Access;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface AccessRepository extends CrudRepository<Access, String > {

    Boolean existsByAccessToken(String access);

    Optional<Access> findOneByAccessToken(String accessToken);

}
