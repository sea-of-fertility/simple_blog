package com.example.simple_blog.repository.post.comment;


import com.example.simple_blog.domain.post.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {


}
