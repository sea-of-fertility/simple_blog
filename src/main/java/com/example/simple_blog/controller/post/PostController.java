package com.example.simple_blog.controller.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.request.post.PostDTO;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.PostService;
import com.example.simple_blog.service.post.file.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/chat-blog")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final StorageService storageService;
    private final MemberService memberService;

    @PostMapping("/post")
    @PreAuthorize("hasRole('USER')")
    public void post(@AuthenticationPrincipal UserDetails userDetails,@RequestPart PostDTO postDTO, @RequestPart(required = false) List<MultipartFile> multipartFiles) throws MemberNotFoundException {

        String address = userDetails.getUsername();
        log.info("getUserName{}", userDetails.getUsername());
        log.info("getUser password{}", userDetails.getPassword());
        log.info("getUser authorities{}", userDetails.getAuthorities());

        Member member = memberService.findByAddress(address);

        Post post = postDTO.toEntity(member);

        if(multipartFiles != null){
            multipartFiles.forEach((m -> {
                FilePath store = storageService.store(m, address);
                post.saveFilePath(store);
            }));
        }

        postService.save(member.getId(), post);
    }



}
