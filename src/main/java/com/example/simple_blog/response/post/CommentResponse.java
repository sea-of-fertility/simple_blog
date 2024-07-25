package com.example.simple_blog.response.post;

import com.example.simple_blog.domain.post.Comment;
import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentResponse  extends RepresentationModel<CommentResponse>{

    private String content;
    private String author;
    private Long postId;
    private LocalDate createTime;
    private Long parentId;
    private List<CommentResponse> children = new ArrayList<>();


    @Builder
    public CommentResponse(String content, String author, Long post, Comment parent, LocalDate createTime) {
        this.content = content;
        this.author = author;
        this.createTime = createTime;
        this.postId = post;
        this.parentId =  parent == null? null : parent.getId();
    }
}
