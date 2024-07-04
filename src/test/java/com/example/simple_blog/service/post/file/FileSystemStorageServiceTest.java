package com.example.simple_blog.service.post.file;

import com.example.simple_blog.config.properties.StorageProperties;
import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.FileRepository;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.service.post.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;


@SpringBootTest
class FileSystemStorageServiceTest {
    private String exampleTitle = "title";
    private String exampleContent = "title";
    private String testPassword = "1234@121a";
    private String testAddress = "hello@naver.com";

    @Autowired
    FileSystemStorageService storageService;


    @Autowired
    StorageProperties storageProperties;

    @Autowired
    PostService postService;

    @Autowired
    MemberRepository memberRepository;


    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;
    String testEmail = "test@email.com";

    @AfterEach
    void setDirectory() {
        storageService.deleteAll();

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

    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
            "text/plain", "Spring Framework".getBytes());


    @Test
    @DisplayName("파일 저장")
    public void fileSave() throws Exception {

        Member member = memberRepository.findByAddress(testAddress).get();

        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();

        postRepository.save(post);

        storageService.store(multipartFile, testEmail, post);

        Path path = Paths.get(storageProperties.getLocation());
        path = path.resolve(testEmail).resolve(String.valueOf(post.getId()));
        File file = new File(String.valueOf(path));
        File[] files = file.listFiles();

        assert files != null;
        Assertions.assertThat(Arrays.stream(files).count()).isEqualTo(1);

    }

    @Test
    @DisplayName("게시글 하나 삭제하기")
    void deletePost() throws Exception {
        Member member = memberRepository.findByAddress(testAddress).get();

        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .member(member)
                .build();

        postRepository.save(post);


        //given
        storageService.store(multipartFile, testEmail, post);


        //when
        postRepository.deleteById(post.getId());
        storageService.delete(post);


        //then
        Assertions.assertThat(0L).isEqualTo(postRepository.count());
        Assertions.assertThat(0L).isEqualTo(fileRepository.count());
    }
}