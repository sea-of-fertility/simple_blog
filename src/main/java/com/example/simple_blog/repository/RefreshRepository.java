package com.example.simple_blog.repository;

import com.example.simple_blog.domain.token.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByUserAddress(String address);

    @Transactional
    void deleteByRefresh(String refresh);
}
