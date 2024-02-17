package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.InvalidEmailException;
import com.example.simple_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Member save(Member member) {
        String address = member.getAddress();

        if (EmailValidator.isValidEmail(address))
            return memberRepository.save(member);

        throw new InvalidEmailException();
    }
}
