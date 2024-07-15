package com.example.simple_blog.repository.post;

import com.example.simple_blog.domain.post.FilePath;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FileRepository extends JpaRepository<FilePath, Long> {

    List<FilePath> findByPostId(Long id);

    Boolean existsByPostId(Long id);
}
