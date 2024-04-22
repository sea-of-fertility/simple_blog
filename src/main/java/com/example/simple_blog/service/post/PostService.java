package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberService memberService;


    @PreAuthorize("hasAuthority('USER')")
    public void post(Long memberId, Post post) throws MemberNotFoundException {
        postRepository.save(post);
    }
}
