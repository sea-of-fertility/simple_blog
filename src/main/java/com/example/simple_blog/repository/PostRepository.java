package com.example.simple_blog.repository;

import com.example.simple_blog.domain.post.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {


    Page<Post> findAll(Pageable pageable);
}
