package com.example.simple_blog.config;

import com.example.simple_blog.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

@Getter
public class MemberDetail extends User {

    private final Long memberId;

    public MemberDetail(Member member) {
        super(member.getMemberName(), member.getPassword(), List.of(new SimpleGrantedAuthority(member.getRole())));
        this.memberId = member.getId();
    }


}
