package com.example.simple_blog.response.post.comment;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
public class CommentsResponse extends RepresentationModel<CommentsResponse>{

    private final Long postId;
    private List<Comments> comments = new ArrayList<>();

    @Builder
    public CommentsResponse(Long postId, List<Comments> comments) {
        this.postId = postId;
        this.comments = comments;
    }
}
