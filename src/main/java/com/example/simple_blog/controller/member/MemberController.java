package com.example.simple_blog.controller.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.request.member.JoinDto;
import com.example.simple_blog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.ContentHandler;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public String member() {
        log.info("current authentication{}", SecurityContextHolder.getContext().getAuthentication());
        return "hello member";
    }

    @PostMapping("/join")
    public void MemberJoin(@RequestBody @Validated JoinDto joinDto) {
        Member member = joinDto.toEntity();
        Member save = memberService.save(member);
    }

    @PostMapping("/changed/pwd")
    public void ChangePWD(Member member, String newPWD) {
        memberService.passwordChange(member, newPWD);
    }


}