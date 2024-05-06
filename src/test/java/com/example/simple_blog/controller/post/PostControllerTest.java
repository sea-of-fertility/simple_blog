package com.example.simple_blog.controller.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.request.post.PostDTO;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.PostService;
import com.example.simple_blog.service.post.file.FileSystemStorageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    final String testTitle = "tile";
    final String testContent = "content";
    final String testAddress = "hello@gmail.com";

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FileSystemStorageService storageService;


    @BeforeEach
    public void setRepository() {
        Member build = Member.builder()
                .address(testAddress)
                .role("ROLE_USER")
                .memberNickName("nick")
                .password("hdfdf@1")
                .memberName("realName")
                .build();
        memberRepository.save(build);
    }

    @AfterEach
    public void resetRepository() {
        storageService.deleteAll();

        memberRepository.deleteAll();
        postRepository.deleteAll();
    }

    @Test
    @WithMockUser(username = testAddress, roles = "USER")
    @DisplayName("게시글만 저장")
    public void post() throws Exception {

        //given
        PostDTO postDTO = PostDTO.builder()
                .title(testTitle)
                .content(testContent)
                .build();

        String json = objectMapper.writeValueAsString(postDTO);
        MockMultipartFile post = new MockMultipartFile("postDTO",
                "", "application/json", json.getBytes());

           //expect
        mockMvc.perform(multipart("/chat-blog/user/post")
                        .file(post))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @WithMockUser(username = testAddress, roles = "USER")
    @DisplayName("게시글에 파일 첨부")
    public void postWithFile() throws Exception {

        //given
        PostDTO postDTO = PostDTO.builder()
                .title(testTitle)
                .content(testContent)
                .build();

        String json = objectMapper.writeValueAsString(postDTO);

        MockMultipartFile post = new MockMultipartFile("postDTO",
                "", "application/json", json.getBytes());

        MockMultipartFile multipartFile = new MockMultipartFile("multipartFiles", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        //expect
        mockMvc.perform(multipart("/chat-blog/user/post")
                .file(multipartFile)
                .file(post))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    @Test
    @DisplayName("게시글 단건 조회 하기")
    void getPost() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title(testTitle)
                .content(testContent)
                .member(member)
                .build();
        Long id = postService.save(post);

        MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
                "text/plain", "Spring Framework".getBytes());

        FilePath store = storageService.store(multipartFile, member.getAddress(), post);
        System.out.println(store.getPost().getId());
        //expect
        mockMvc.perform(get("/chat-blog/public/{memberId}/{postId}", member.getId(), id))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithMockUser(username = testAddress, roles = "USER")
    @DisplayName("게시글 삭제하기")
    void delete() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();
        Post post = Post.builder()
                .title(testTitle)
                .content(testContent)
                .member(member)
                .build();
        Long id = postService.save(post);

        //expect
        mockMvc.perform(MockMvcRequestBuilders.delete("/chat-blog/user/{postId}", id))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("여러개의 게시글 조회하기")
    void getPosts() throws Exception {
        //given
        Member member = memberRepository.findByAddress(testAddress).get();

        for (int i = 0; i < 1; i++) {
            Post post = Post.builder()
                    .title(testTitle + String.valueOf(i))
                    .content(testContent)
                    .member(member)
                    .build();
            Long id = postService.save(post);
        }

        //expect
        mockMvc.perform(get("/chat-blog/public/{memberId}", member.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
