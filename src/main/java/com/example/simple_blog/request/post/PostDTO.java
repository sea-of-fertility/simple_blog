package com.example.simple_blog.request.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Post;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

@Getter
public class PostDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String content;


    @Builder
    public PostDTO(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public Post toEntity(Member member) {
        return Post.builder()
                .title(this.title)
                .content(this.content)
                .member(member)
                .build();
    }

}
