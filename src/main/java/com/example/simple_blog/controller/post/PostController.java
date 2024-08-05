package com.example.simple_blog.controller.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.dto.response.post.*;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.exception.post.UnauthorizedException;
import com.example.simple_blog.dto.request.post.EditeDTO;
import com.example.simple_blog.dto.request.post.PostDTO;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.CommentService;
import com.example.simple_blog.service.post.PostService;
import com.example.simple_blog.service.post.file.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/chat-blog")
@Slf4j
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final FileSystemStorageService storageService;
    private final MemberService memberService;
    private final CommentService commentService;


    @PostMapping("/user/post")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<PostResponse> post(@AuthenticationPrincipal UserDetails userDetails,
                                             @RequestPart PostDTO postDTO,
                                             @RequestPart(required = false) List<MultipartFile> multipartFiles)
            throws MemberNotFoundException {

        String address = userDetails.getUsername();

        Member member = memberService.findByAddress(address);

        Post post = postDTO.toEntity(member);
        postService.save(post);

        PostResponse postResponse = PostResponse
                .builder().postDTO(postDTO)
                .author(userDetails.getUsername())
                .build();

        if(multipartFiles != null){
            log.info("path {}", multipartFiles.size());
            multipartFiles.forEach((m -> {
                if (!m.isEmpty()) {
                    FilePath store = storageService.store(m, address, post);
                    postResponse.setPaths(store.getFilePath());
                    log.info("path {}", store.getFilePath());
                }
            }));
        }

        postResponse.add(linkTo(methodOn(PostController.class).post(userDetails, postDTO, multipartFiles)).withSelfRel());
        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }


    @PatchMapping("/user/post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<EditeResponse> edite(@AuthenticationPrincipal UserDetails userDetails,
                                               @PathVariable(name = "postId") Long postId,
                                               @RequestBody EditeDTO editeDTO) {

        String username = userDetails.getUsername();
        Post byId = postService.findById(postId);

        if (!byId.getMember().getAddress().equals(username))
            throw new UnauthorizedException();

        postService.edit(postId, Post.builder()
                .content(editeDTO.getContent())
                .title(editeDTO.getTitle())
                .build());

        EditeResponse editeResponse = EditeResponse.builder()
                .title(editeDTO.getTitle())
                .content(editeDTO.getContent())
                .build().add(linkTo(methodOn(PostController.class)
                        .edite(userDetails, postId, editeDTO))
                        .withSelfRel());

        return new ResponseEntity<>(editeResponse, HttpStatus.OK);
    }


    @DeleteMapping("/user/post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DeleteResponse> delete(@AuthenticationPrincipal UserDetails userDetails,
                                                 @PathVariable(name = "postId") Long postId) {
        Post post = postService.get(postId);
        if (!userDetails.getUsername().equals(post.getMember().getAddress()))
            throw new UnauthorizedException();

        postService.delete(post);

        DeleteResponse deleteResponse = DeleteResponse.builder()
                .deleteBy(userDetails.getUsername())
                .build()
                .add(linkTo(methodOn(PostController.class)
                        .delete(userDetails, postId))
                        .withSelfRel());

        return new ResponseEntity<>(deleteResponse, HttpStatus.NO_CONTENT);
    }


    @GetMapping("/public/{memberId}/{postId}")
    public ResponseEntity<GetResponse> getOnePost(@PathVariable("memberId") Long memberId,
                                                  @PathVariable("postId") Long postId) {
        Post post = postService.findById(postId);
        List<String> load = storageService.load(post.getId());
        GetResponse getResponse = GetResponse.builder()
                .post(post)
                .paths(load)
                .build();
            log.info("file path {}",load);
        getResponse.add(linkTo(methodOn(PostController.class).getOnePost(memberId, postId)).withSelfRel());

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    @GetMapping("/public/{memberId}")
    public ResponseEntity<GetPostsResponse> getAllPosts(@PathVariable Long memberId,
                                                        @RequestParam(name = "lastIndex", required = false) Long lastIndex)
        throws MemberNotFoundException {

        lastIndex = lastIndex == null? postService.getLatestPostIdByMemberId(memberId): lastIndex;

        List<Post> posts = postService.getPosts(lastIndex);

        GetPostsResponse getPostsResponse = GetPostsResponse.builder()
                .posts(posts)
                .build();

        getPostsResponse.add(linkTo(methodOn(PostController.class).getAllPosts(memberId, lastIndex)).withSelfRel());

        return new ResponseEntity<>(getPostsResponse, HttpStatus.OK);
    }


}
