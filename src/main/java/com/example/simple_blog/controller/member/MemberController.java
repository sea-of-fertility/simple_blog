package com.example.simple_blog.controller.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.request.member.JoinDto;
import com.example.simple_blog.request.member.NewPwdDTO;
import com.example.simple_blog.request.member.PasswordDTO;
import com.example.simple_blog.response.member.MemberResponse;
import com.example.simple_blog.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/chat-blog")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;


    @GetMapping("/public/")
    public String home() {
        return "hello home";
    }

    @PostMapping("/public/join")
    public HttpEntity<MemberResponse> memberJoin(@RequestBody @Validated JoinDto joinDto) {

        Member member = joinDto.toEntity();
        Member save = memberService.save(member);

        MemberResponse memberResponse = MemberResponse.builder()
                .address(save.getAddress())
                .memberNickName(save.getMemberNickName())
                .build();

        memberResponse.add(linkTo(methodOn(MemberController.class).memberJoin(joinDto)).withSelfRel());
        return new ResponseEntity<>(memberResponse, HttpStatus.CREATED);
    }

    @DeleteMapping("/user/member")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> delete(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PasswordDTO passwordDTO) throws MemberNotFoundException {

        String address = userDetails.getUsername();
        Member member = memberService.findByAddress(address);
        memberService.delete(member, passwordDTO.getPassword());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @PatchMapping("/user/member/changed/pwd")
    @PreAuthorize("hasRole('USER')")
    public HttpEntity<Void> ChangePWD(@AuthenticationPrincipal UserDetails userDetails, @RequestBody NewPwdDTO changePWDDto) throws MemberNotFoundException {
        String address = userDetails.getUsername();

        Member member = memberService.findByAddress(address);
        log.info("findMember {}",member);
        log.info("address {}", address);
        log.info("getBeforePassword {}",changePWDDto.getBeforePassword());
        log.info("getAfterPassword() {}",changePWDDto.getAfterPassword());

        memberService.passwordChange(member, changePWDDto.getBeforePassword(), changePWDDto.getAfterPassword());

        return  ResponseEntity.ok().build();
    }



}