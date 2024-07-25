package com.example.simple_blog.response.post;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Getter
public class CommentsResponse extends RepresentationModel<CommentsResponse>{



    private Long postId;

    private List<Comments> comments = new ArrayList<>();

    @Builder
    public CommentsResponse(Long postId, List<CommentResponse> comments) {
        this.postId = postId;
        setComments(comments);
    }

    private void setComments(List<CommentResponse> comments) {
        List<Comments> list = comments.stream().map(comment -> Comments.builder()
                .author(comment.getAuthor())
                .content(comment.getContent())
                .create(comment.getCreateTime())
                .parentId(comment.getPostId())
                .build()).toList();
    }


    static class Comments {
        private String author;
        private String content;
        private LocalDate create;
        private Long parentId;

        @Builder
        public Comments(String author, String content, LocalDate create, Long parentId) {
            this.author = author;
            this.content = content;
            this.create = create;
            this.parentId = parentId;
        }
    }

}
