package com.example.simple_blog.dto.request.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import lombok.Builder;
import lombok.Getter;


@Getter
public class CommentDTO {
    private Long postId;
    private Long parentId;
    private String content;

    @Builder
    public CommentDTO(Long postId, String content, Long parentId) {
        this.postId = postId;
        this.parentId = parentId;
        this.content = content;

    }

     public Comment toEntity(Member member, Post post, Comment parent) {
        return Comment.builder()
                .author(member)
                .post(post)
                .parent(parent)
                .content(this.content)
                .build();
    }
}
