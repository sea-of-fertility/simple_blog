package com.example.simple_blog.repository;

import com.example.simple_blog.domain.post.FilePath;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<FilePath, Long> {

    List<FilePath> findByPostId(Long id);

    Boolean existsByPostId(Long id);
}
