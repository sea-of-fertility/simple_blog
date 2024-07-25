package com.example.simple_blog.repository.post.comment;

import com.example.simple_blog.domain.post.Comment;

import java.util.List;

public interface CommentRepositoryCustom {

    List<Comment> getComments(Long id, Long lastIndex);

    Long getFirstComment(Long postId);
}
