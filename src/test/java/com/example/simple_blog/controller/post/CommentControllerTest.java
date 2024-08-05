package com.example.simple_blog.controller.post;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.member.MemberRepository;
import com.example.simple_blog.repository.post.PostRepository;
import com.example.simple_blog.repository.post.comment.CommentRepository;
import com.example.simple_blog.dto.request.post.CommentDTO;
import com.example.simple_blog.service.member.MemberService;
import com.example.simple_blog.service.post.CommentService;
import com.example.simple_blog.service.post.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.stream.IntStream;


@SpringBootTest
@AutoConfigureMockMvc
class CommentControllerTest {


    String testTitle = "title";
    String testContext = "context";

    final String nickName = "nick";
    final String memberName = "hello";
    final String testAddress = "hello@naver.com";
    final String testPassword = "1234@121a";


    @Autowired
    CommentRepository commentRepository;


    @Autowired
    MockMvc mockMvc;

    @Autowired
    CommentService commentService;

    @Autowired
    MemberService memberService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    ObjectMapper objectMapper;


    @AfterEach
    @BeforeEach
    void deleteRepository() {
        memberRepository.deleteAll();
        postRepository.deleteAll();
        commentRepository.deleteAll();
    }


    @Test
    @DisplayName("첫 댓글 작성하기")
    @WithMockUser(username = testAddress, roles = "USER")
    void createComment() throws Exception {
        Member member = getMember();
        Post post = getPost();

        //given
        String json = objectMapper.writeValueAsString(CommentDTO.builder()
                        .postId(post.getId())
                        .parentId(null)
                        .content(testContext)
                        .build());

        //expect
        this.mockMvc.perform(MockMvcRequestBuilders.post("/chat-blog/user/comment")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(testContext))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value(member.getMemberNickName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content").value(testContext))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @DisplayName("특정 게시글 댓글 모아보기")
    void getComment() throws Exception {
        //given
        Member member = getMember();
        Post post = getPost();

        List<Comment> comments = commentRepository.saveAll(IntStream.range(0, 5).mapToObj(i -> Comment.builder()
                .author(member)
                .parent(null)
                .post(post)
                .content("parent" + i)
                .build()).toList());

        List<Comment> reComment = commentRepository.saveAll(IntStream.range(0, 5).mapToObj(i -> Comment.builder()
                .author(member)
                .parent(comments.get(i))
                .post(post)
                .content("children" + i)
                .build()).toList());


        //expect
        this.mockMvc.perform(MockMvcRequestBuilders.get("/chat-blog/public/comments/{postId}", post.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    Member getMember() {
        Member nick = Member.builder()
                .address(testAddress)
                .memberNickName(nickName)
                .memberName(memberName)
                .password(testPassword)
                .build();
        return memberService.save(nick);
    }

    private Post getPost() {


        Member byAddress = memberService.findByAddress(testAddress);
        Post post = Post.builder()
                .title(testTitle)
                .content(testContext)
                .member(byAddress)
                .build();
        postService.save(post);
        return post;
    }
}