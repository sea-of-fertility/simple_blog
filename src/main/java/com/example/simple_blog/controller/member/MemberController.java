package com.example.simple_blog.controller.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.request.member.JoinDto;
import com.example.simple_blog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/join")
    public void MemberJoin(@RequestBody @Validated JoinDto joinDto) {
        Member member = joinDto.toEntity(passwordEncoder);
        Member save = memberService.save(member);
    }


}
