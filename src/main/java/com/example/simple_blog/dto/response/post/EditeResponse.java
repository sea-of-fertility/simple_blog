package com.example.simple_blog.dto.response.post;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
@Builder
public class EditeResponse extends RepresentationModel<EditeResponse> {

    private String title;
    private String content;
}
