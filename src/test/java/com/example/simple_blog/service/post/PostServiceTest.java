package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.FileRepository;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.service.post.file.StorageService;
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
    StorageService storageService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;
    @Autowired
    private MemberRepository memberRepository;

    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
            "text/plain", "Spring Framework".getBytes());



    @AfterEach
    public void setRepository() {
        postRepository.deleteAll();
        memberRepository.deleteAll();
        fileRepository.deleteAll();
    }


    @BeforeEach
    public void setMemberRepository() {
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
    public void post() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();

        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();

        //when
        postService.save(member.getId(), post);

        //then
        Assertions.assertThat(postRepository.count()).isEqualTo(1);
    }


    @Test
    @DisplayName("@Notempty에 값이 없을 경우 예외를 발생한다.")
    public void noTitle() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title("")
                .member(member)
                .content(exampleContent)
                .build();

        //expect
        Assertions.assertThatThrownBy(() -> postService.save(member.getId(), post))
                        .isInstanceOf(ConstraintViolationException.class);
    }


//    @Test
    @DisplayName("파일 추가해서 올리기")
    public void filePost() throws Exception {

        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();

        //when
        FilePath store = storageService.store(multipartFile, member.getAddress());
        FilePath store1 = storageService.store(multipartFile, member.getAddress());
        FilePath store2 = storageService.store(multipartFile, member.getAddress());
        post.saveFilePath(store1);
        post.saveFilePath(store2);
        post.saveFilePath(store);
        postService.save(member.getId(), post);

        //then
        Assertions.assertThat(postRepository.findAll().size()).isEqualTo(1L);

    }

}