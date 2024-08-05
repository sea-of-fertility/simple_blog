package com.example.simple_blog.dto.service.post.comment;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
public class CommentsDTO {
    private final Long postId;
    private final Long commentId;
    private final Long parentId;
    private final String content;
    private final String author;
    private final LocalDate createTime;
    private final List<CommentsDTO> children = new ArrayList<>();

    @Builder
    public CommentsDTO(Long parentId, String content, Long postId, LocalDate createTime, String author, Long commentId) {
        this.parentId = parentId;
        this.commentId = commentId;
        this.content = content;
        this.postId = postId;
        this.createTime = createTime;
        this.author = author;
    }
}