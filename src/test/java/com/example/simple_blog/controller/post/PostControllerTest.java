package com.example.simple_blog.controller.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.request.post.PostDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

@SpringBootTest
@AutoConfigureMockMvc
class PostControllerTest {

    final String title = "tile";
    final String content = "content";
    final String userAddress = "hello@gmail.com";
    @Autowired
    MemberRepository memberRepository;


    @BeforeEach
    public void setRepository() {
        Member build = Member.builder()
                .address(userAddress)
                .role("ROLE_USER")
                .memberNickName("nick")
                .password("hdfdf@1")
                .memberName("realName")
                .build();
        memberRepository.save(build);
    }

    @AfterEach
    public void resetRepository() {
        memberRepository.deleteAll();
    }


    @Test
    @WithMockUser(username = userAddress, roles = "USER")
    public void post() throws Exception {
        PostDTO postDTO = PostDTO.builder()
                .title(title)
                .content(content)
                .build();



        //given

        //when

        //then
    }

}