package com.example.simple_blog.response.post;

import com.example.simple_blog.domain.post.Post;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetPostsResponse {
    private  List<SimplePost> posts = new ArrayList<>();


    public void add(Post post) {
        SimplePost build = SimplePost.builder()
                .title(post.getTitle())
                .content(post.getContent())
                .build();
        this.posts.add(build);
    }




}
