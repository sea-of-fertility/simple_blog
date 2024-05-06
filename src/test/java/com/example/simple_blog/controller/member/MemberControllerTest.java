package com.example.simple_blog.controller.member;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.request.member.NewPwdDTO;
import com.example.simple_blog.request.member.JoinDto;
import com.example.simple_blog.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    private String testPassword = "a12345678@";
    private String testChangePassword = "new145678@";
    final String userAddress = "hello@gmail.com";

    @AfterEach
    public void setMemberRepository() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("가입 테스트")
    public void joinTest() throws Exception {

        //given
        JoinDto nick = JoinDto.builder()
                .address("hello@naver.com")
                .memberNickName("nickaa")
                .memberName("hello")
                .password(testPassword)
                .build();


        //when
        String json = objectMapper.writeValueAsString(nick);

        //given
        mockMvc.perform(post("/chat-blog/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(username = userAddress)
    @DisplayName("비밀번호 변경하기")
    public void pwdChange() throws Exception {

        Member build = Member.builder()
                .address(userAddress)
                .role("ROLE_USER")
                .memberNickName("nick")
                .password(testPassword)
                .memberName("realName")
                .build();
        memberService.save(build);

        NewPwdDTO build1 = NewPwdDTO.builder()
                .beforePassword(testPassword)
                .afterPassword(testChangePassword)
                .build();

        String s = objectMapper.writeValueAsString(build1);

        mockMvc.perform(MockMvcRequestBuilders.patch("/chat-blog/changed/pwd")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(s))
                .andDo(print())
                .andExpect(status().isOk());


    }



}