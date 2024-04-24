package com.example.simple_blog.controller.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.request.member.ChangePWDDto;
import com.example.simple_blog.request.member.JoinDto;
import com.example.simple_blog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.ContentHandler;

@RestController
@RequestMapping("/chat-blog")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/")
    public String home() {
        return "hello home";
    }

    @PostMapping("/join")
    public void MemberJoin(@RequestBody @Validated JoinDto joinDto) {
        Member member = joinDto.toEntity();
        Member save = memberService.save(member);
    }

    @PostMapping("/changed/pwd")
    @PreAuthorize("hasRole('USER')")
    public void ChangePWD(@AuthenticationPrincipal UserDetails userDetails, @RequestBody  ChangePWDDto changePWDDto) throws MemberNotFoundException {
        String address = userDetails.getUsername();

        Member member = memberService.findByAddress(address);
        log.info("findMember {}",member);
        log.info("address {}", address);
        log.info("getBeforePassword {}",changePWDDto.getBeforePassword());
        log.info("getAfterPassword() {}",changePWDDto.getAfterPassword());

        memberService.passwordChange(member, changePWDDto.getBeforePassword(), changePWDDto.getAfterPassword());
    }


}