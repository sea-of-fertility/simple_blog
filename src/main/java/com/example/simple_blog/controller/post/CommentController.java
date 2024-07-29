package com.example.simple_blog.controller.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.request.post.CommentDTO;
import com.example.simple_blog.response.post.comment.CommentResponse;
import com.example.simple_blog.response.post.comment.Comments;
import com.example.simple_blog.response.post.comment.CommentsResponse;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.CommentService;
import com.example.simple_blog.service.post.PostService;
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
    private final PostService postService;

    @PostMapping("/user/comment")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommentResponse> comment(@AuthenticationPrincipal UserDetails userDetails,
                                                   @RequestBody CommentDTO commentDto)
    {
        Member member = memberService.findByAddress(userDetails.getUsername());
        Comment parent = commentDto.getParentId() != null? commentService.findByParentId(commentDto.getParentId()): null;
        Post post = postService.findById(commentDto.getPostId());

        Comment comment = commentService.save(commentDto.toEntity(member, post, parent));
        CommentResponse commentResponse = CommentResponse.builder()
                .parent(comment.getParent())
                .commentId(comment.getId())
                .content(comment.getContent())
                .createTime(comment.getCreateTime())
                .author(member.getMemberNickName())
                .post(comment.getPost().getId())
                .build()
                .add(linkTo(methodOn(CommentController.class).comment(userDetails, commentDto)).withSelfRel());

        return new ResponseEntity<>(commentResponse, HttpStatus.OK);
    }


    @GetMapping("/public/comments/{postId}")
    public ResponseEntity<CommentsResponse> comments(@PathVariable Long postId) {
        List<Comments> comments = commentService.getComments(postId);
        CommentsResponse commentsResponse = CommentsResponse.builder()
                .comments(comments)
                .postId(postId)
                .build()
                .add(linkTo(methodOn(CommentController.class).comments(postId)).withSelfRel());
        return new ResponseEntity<>(commentsResponse, HttpStatus.OK);
    }

}
