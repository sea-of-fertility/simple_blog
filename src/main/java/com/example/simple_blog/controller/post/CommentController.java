package com.example.simple_blog.controller.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.request.post.CommentDTO;
import com.example.simple_blog.response.post.CommentResponse;
import com.example.simple_blog.response.post.CommentsResponse;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RequestMapping("/chat-blog")
@RestController
@RequiredArgsConstructor
@Slf4j
public class CommentController {


    private final CommentService commentService;
    private final MemberService memberService;


    @PostMapping("/user/comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResponse> comment(@AuthenticationPrincipal UserDetails userDetails, @RequestBody CommentDTO commentDto) {
        Member member = memberService.findByAddress(userDetails.getUsername());
        Comment save = commentService.save(commentDto.toEntity(member));
        CommentResponse commentResponse = CommentResponse.builder()
                .parent(save.getParent())
                .content(save.getContent())
                .author(member.getMemberNickName())
                .post(save.getPost().getId())
                .build()
                .add(linkTo(methodOn(CommentController.class).comment(userDetails, commentDto)).withSelfRel());

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }

    @GetMapping("/public/comments/{postId}")
    public ResponseEntity<List<CommentResponse>> comments(@PathVariable Long postId) {
        List<CommentResponse> comments = commentService.getComments(postId);
        return new ResponseEntity<>(comments, HttpStatus.OK);
    }

}
