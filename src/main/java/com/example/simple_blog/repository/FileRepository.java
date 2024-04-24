package com.example.simple_blog.repository;

import com.example.simple_blog.domain.post.FilePath;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FilePath, Long> {
}
