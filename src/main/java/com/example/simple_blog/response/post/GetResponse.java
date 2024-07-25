package com.example.simple_blog.response.post;

import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class GetResponse extends RepresentationModel<GetResponse> {

    private Long id;
    private String title;
    private String content;
    private String author;
    private LocalDate createTime;
    private List<String> paths = new ArrayList<>();

    @Builder
    public GetResponse(Post post, List<String> paths) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getMember().getAddress();
        this.createTime = post.getCreateTime();
        this.paths = paths;
    }


}
