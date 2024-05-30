package com.example.simple_blog.repository;

import com.example.simple_blog.domain.token.Refresh;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RefreshRepository extends JpaRepository<Refresh, Long> {

    Boolean existsByRefresh(String refresh);

    Boolean existsByMemberAddress(String address);


    @Transactional
    void deleteByMemberAddress(String address);

    @Transactional
    void deleteByRefresh(String refresh);
}
