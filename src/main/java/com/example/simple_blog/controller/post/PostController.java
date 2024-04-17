package com.example.simple_blog.controller.post;


import com.example.simple_blog.request.post.PostDTO;
import com.example.simple_blog.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping("/post")
    public void post(@AuthenticationPrincipal UserDetails userDetails, PostDTO postDTO, @RequestPart(required = false) List<MultipartFile> multipartFiles) {


    }


}
