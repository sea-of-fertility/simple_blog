package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.FileRepository;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.service.post.file.FileSystemStorageService;
import jakarta.validation.ConstraintViolationException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

@SpringBootTest
class PostServiceTest {
    private String exampleTitle = "title";
    private String exampleContent = "title";
    private String testPassword = "1234@121a";
    private String testAddress = "hello@naver.com";

    @Autowired
    PostService postService;

    @Autowired
    FileSystemStorageService storageService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;
    @Autowired
    private MemberRepository memberRepository;

    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
            "text/plain", "Spring Framework".getBytes());



    @AfterEach
    void setRepository() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
        fileRepository.deleteAll();
    }


    @BeforeEach
    void setMemberRepository() {
        Member nick = Member.builder()
                .address(testAddress)
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();
        memberRepository.save(nick);
    }


    @Test
    @DisplayName("게시글 등록")
    void post() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();

        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();

        //when
        postService.save(post);

        //then
        Assertions.assertThat(postRepository.count()).isEqualTo(1);
    }


    @Test
    @DisplayName("제목이 없을 경우 예외를 발생한다.")
    void noTitle() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title("")
                .member(member)
                .content(exampleContent)
                .build();

        //expect
        Assertions.assertThatThrownBy(() -> postService.save(post))
                        .isInstanceOf(ConstraintViolationException.class);
    }


    @Test
    @DisplayName("게시글 단건 조회하기")
    void getPost() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();
        Post savePost = postService.save(post);
        //when
        Post getPost = postService.get(savePost.getId());

        //then
        Assertions.assertThat(getPost.getId()).isEqualTo(savePost.getId());
    }

    @Test
    @DisplayName("게시글 삭제하기")
    void delete() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();
        Post postId = postService.save(post);

        //when
        postService.delete(postId);

        //then
        Assertions.assertThat(postRepository.count()).isEqualTo(0L);
    }

}