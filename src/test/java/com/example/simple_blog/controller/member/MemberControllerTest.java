package com.example.simple_blog.controller.member;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.service.member.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


    @AfterEach
    public void setMemberRepository() {
        memberRepository.deleteAll();
    }


    @Test
    @DisplayName("가입 테스트")
    public void joinTest() throws Exception {

        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();

        //when
        String json = objectMapper.writeValueAsString(nick);

        //given
        mockMvc.perform(post("/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("비밀번호 변경하기")
    public void pwdChange() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();

        memberService.save(nick);
    }



}