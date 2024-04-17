package com.example.simple_blog.request.post;


import com.example.simple_blog.domain.post.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

public class PostDTO {

    @NotBlank
    private final String title;

    @NotBlank
    private final String content;


    @Builder
    public PostDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }



}
