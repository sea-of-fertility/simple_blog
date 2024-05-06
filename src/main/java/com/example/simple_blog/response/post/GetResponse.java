package com.example.simple_blog.response.post;

import com.example.simple_blog.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetResponse extends RepresentationModel<GetResponse> {

    private String title;
    private String content;
    private String author;

    private List<String> paths = new ArrayList<>();

    @Builder
    public GetResponse(Post post, String author, List<String> paths) {
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = author;
        this.paths = paths;
    }

}
