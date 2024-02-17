package com.example.simple_blog.service;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.InvalidEmailException;
import com.example.simple_blog.exception.member.MemberException;
import com.example.simple_blog.exception.member.MemberNotFoundException;
import com.example.simple_blog.repository.MemberRepository;
import com.example.simple_blog.service.member.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class MemberServiceTest {



    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @AfterEach
    public void setMemberRepository() {
        memberRepository.deleteAll();
    }

    @Test
    @DisplayName("가입 성공")
    public void joinTest() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password("0000")
                .build();
        //when
        Member save = memberService.save(nick);

        //then
        Member byAddress = memberRepository.findByAddress(nick.getAddress()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat(nick.getAddress()).isEqualTo(byAddress.getAddress());
    }

    @Test
    @DisplayName("이메일 형식이 아닌 가입")
    public void joinFail() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hellonaver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password("0000")
                .build();

        //then
        Assertions.assertThatThrownBy(() -> {
            Member save = memberService.save(nick);
        }).isInstanceOfAny(MemberException.class);
    }

}