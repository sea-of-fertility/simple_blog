package com.example.simple_blog.request.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class EditeDTO {
    private String title;
    private String content;

    @Builder
    public EditeDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
