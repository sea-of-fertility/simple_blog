package com.example.simple_blog.response.post;

import lombok.Builder;
import lombok.Getter;

@Getter
public class SimplePost {

    private final String title;
    private final String content;

    @Builder
    public SimplePost(String title, String content) {
        if (title.length() >= 50) {
            title = title.substring(0, 50);
            title += "...";
        }


        if (content.length() >= 50) {
            content = content.substring(0, 50);
            content += "...";
        }

        this.title = title;
        this.content = content;
    }
}
