package com.example.simple_blog.response.post;

import lombok.Builder;
import org.springframework.hateoas.RepresentationModel;

@Builder
public class DeleteResponse extends RepresentationModel<DeleteResponse> {

    private String deleteBy;

}
