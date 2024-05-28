package com.example.simple_blog.controller.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.exception.post.UnauthorizedDeletionException;
import com.example.simple_blog.request.post.EditeDTO;
import com.example.simple_blog.request.post.PostDTO;
import com.example.simple_blog.response.post.*;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.PostService;
import com.example.simple_blog.service.post.file.FileSystemStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpEntity;
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
    public ResponseEntity<EditeResponse> edite(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(name = "postId") Long postId,
                                               @RequestBody EditeDTO editeDTO) {

        String username = userDetails.getUsername();
        Post byId = postService.findById(postId);
        if (byId.getMember().getAddress().equals(username)) {
            postService.edit(postId, Post.builder()
                    .content(editeDTO.getContent())
                    .title(editeDTO.getTitle())
                    .build());
            EditeResponse editeResponse = EditeResponse.builder()
                    .title(editeDTO.getTitle())
                    .content(editeDTO.getContent())
                    .build();

            editeResponse.add(linkTo(methodOn(PostController.class).edite(userDetails, postId, editeDTO)).withSelfRel());
            return new ResponseEntity<>(editeResponse, HttpStatus.OK);
        }
        throw new UnauthorizedDeletionException();
    }


    @DeleteMapping("/user/post/{postId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<DeleteResponse> delete(@AuthenticationPrincipal UserDetails userDetails, @PathVariable(name = "postId") Long postId) {
        Post post = postService.get(postId);
        if (userDetails.getUsername().equals(post.getMember().getAddress())) {
            DeleteResponse deleteResponse = DeleteResponse.builder()
                    .deleteBy(userDetails.getUsername())
                    .build();
            postService.delete(post);
            deleteResponse.add(linkTo(methodOn(PostController.class).delete(userDetails, postId)).withSelfRel());
            return new ResponseEntity<>(deleteResponse, HttpStatus.NO_CONTENT);
        } else {
            throw new UnauthorizedDeletionException();
        }

    }


    @GetMapping("/public/{memberId}/{postId}")
    public ResponseEntity<GetResponse> getOnePost(@PathVariable("memberId") Long memberId, @PathVariable("postId") Long postId) throws MemberNotFoundException {
        Member byMemberId = memberService.findByMemberId(memberId);
        Post post = postService.findById(postId);
        List<String> load = storageService.load(post.getId());
        GetResponse getResponse = GetResponse.builder()
                .author(byMemberId.getAddress())
                .post(post)
                .paths(load)
                .build();
            log.info("file path {}",load);
        getResponse.add(linkTo(methodOn(PostController.class).getOnePost(memberId, postId)).withSelfRel());

        return new ResponseEntity<>(getResponse, HttpStatus.OK);
    }


    @GetMapping("/public/{memberId}")
    public ResponseEntity<GetPostsResponse> getAllPosts(@PathVariable Long memberId,
                                                 @PageableDefault(size = 5, page = 0, sort = "id", direction = Sort.Direction.DESC)
                                                 Pageable pageable) throws MemberNotFoundException {

        Member byMemberId = memberService.findByMemberId(memberId);

        Page<Post> posts = postService.getPosts(pageable);

        GetPostsResponse getPostsResponse = new GetPostsResponse();

        for (Post post : posts) {
            getPostsResponse.add(post);
        }

        return new ResponseEntity<>(getPostsResponse, HttpStatus.OK);
    }

}
