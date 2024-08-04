package com.example.simple_blog.response.post.comment;

import com.example.simple_blog.domain.post.Comment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponse  extends RepresentationModel<CommentResponse>{

    private final String content;
    private final Long commentId;
    private final String author;
    private final Long postId;
    private final LocalDate createTime;
    private final Long parentId;
    private final List<CommentResponse> children = new ArrayList<>();


    @Builder
    public CommentResponse(String content, String author, Long post, Comment parent, LocalDate createTime, Long commentId) {
        this.commentId = commentId;
        this.content = content;
        this.author = author;
        this.createTime = createTime;
        this.postId = post;
        this.parentId =  parent == null? null : parent.getId();
    }
}
