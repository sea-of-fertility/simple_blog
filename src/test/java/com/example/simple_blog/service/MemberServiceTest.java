package com.example.simple_blog.service;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.join.DuplicateNickName;
import com.example.simple_blog.exception.member.join.DuplicatedAddress;
import com.example.simple_blog.exception.member.MemberException;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
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

    private String testPassword = "a12345678@";


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
                .password(testPassword)
                .build();
        //when
        Member save = memberService.save(nick);

        //then
        Member byAddress = memberRepository.findByAddress(nick.getAddress()).orElseThrow(MemberNotFoundException::new);
        Assertions.assertThat(nick.getAddress()).isEqualTo(byAddress.getAddress());
    }


    @Test
    @DisplayName("중복된 이메일")
    public void duplicatedAddress() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();
        //when
        Member save = memberService.save(nick);

        //then
        Assertions.assertThatThrownBy(() -> {
            Member duplicate = Member.builder()
                    .address("hello@naver.com")
                    .memberNickName("nick")
                    .memberName("hello")
                    .password(testPassword)
                    .build();
            memberService.save(nick);
        }).isInstanceOf(DuplicatedAddress.class);
    }

    @Test
    @DisplayName("중복된 닉네임")
    public void duplicateNickName() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello12@naver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();
        //when
        Member save = memberService.save(nick);

        //then
        Assertions.assertThatThrownBy(() -> {
                    Member duplicate = Member.builder()
                            .address("hello41@naver.com")
                            .memberNickName("nick")
                            .memberName("hello")
                            .password(testPassword)
                            .build();
                    memberService.save(duplicate);
                }).isInstanceOf(DuplicateNickName.class);
    }

    @Test
    @DisplayName("잘못된 이메일 형식")
    public void notAddressType() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hellonaver.com")
                .memberNickName("nick")
                .memberName("hello")
                .password(testPassword)
                .build();

        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.save(nick);
        }).isInstanceOfAny(MemberException.class);
    }

    @Test
    @DisplayName("잘못된 닉네임 형식")
    public void invalidNickName() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick!!") // 특수문자가 들어간 잘못된 닉네임 형식
                .memberName("hello")
                .password(testPassword)
                .build();

        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.save(nick);
        }).isInstanceOfAny(MemberException.class);
    }

    @Test
    @DisplayName("잘못된 비밀번호 형식")
    public void invalidPassword() throws Exception {
        //given
        Member nick = Member.builder()
                .address("hello@naver.com")
                .memberNickName("nick!!") // 특수문자가 들어간 잘못된 닉네임 형식
                .memberName("hello")
                .password("1234")
                .build();

        //then
        Assertions.assertThatThrownBy(() -> {
            memberService.save(nick);
        }).isInstanceOfAny(MemberException.class);
    }
}