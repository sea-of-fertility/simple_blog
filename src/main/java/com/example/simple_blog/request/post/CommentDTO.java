package com.example.simple_blog.request.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CommentDTO {
    private String content;
    private Post post;
    private Comment parent;

    @Builder
    public CommentDTO(Post post, String content) {
        this.post = post;
        this.content = content;
    }

     public Comment toEntity(Member member) {
        return Comment.builder()
                .content(this.content)
                .post(this.post)
                .parent(this.parent)
                .author(member)
                .build();
    }
}
