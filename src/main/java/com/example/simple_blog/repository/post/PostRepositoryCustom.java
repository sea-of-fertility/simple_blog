package com.example.simple_blog.repository.post;

import com.example.simple_blog.domain.post.Post;

import java.util.List;

public interface PostRepositoryCustom {

    List<Post> getPosts(Long lastIndex);

    Long findLatestPostIdByMemberId(Long memberId);
}
