package com.example.simple_blog.response.post;

import com.example.simple_blog.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;

@Getter
public class GetPostsResponse extends RepresentationModel<GetPostsResponse> {
    private final List<SimplePost> posts = new ArrayList<>();

    @Builder
    public GetPostsResponse(List<Post> posts) {
        posts.forEach((post) -> {
            SimplePost build = SimplePost.builder()
                    .title(post.getTitle())
                    .content(post.getContent())
                    .build();
            this.posts.add(build);
        });

    }
}
