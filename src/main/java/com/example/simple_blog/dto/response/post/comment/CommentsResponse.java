package com.example.simple_blog.dto.response.post.comment;

import com.example.simple_blog.dto.service.post.comment.CommentsDTO;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.util.ArrayList;
import java.util.List;


@Getter
@Builder
public class CommentsResponse extends RepresentationModel<CommentsResponse>{

    private final Long postId;
    private List<CommentsDTO> comments = new ArrayList<>();

    @Builder
    public CommentsResponse(Long postId, List<CommentsDTO> comments) {
        this.postId = postId;
        this.comments = comments;
    }
}
