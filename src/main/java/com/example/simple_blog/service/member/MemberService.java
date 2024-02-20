package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.join.DuplicatedAddress;
import com.example.simple_blog.exception.member.join.InvalidEmailException;
import com.example.simple_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(Member member) {
        String address = member.getAddress();

        if(!JoinValidator.isValidEmail(address))
            throw new InvalidEmailException();

        else if (memberRepository.existsByAddress(address)) {
            throw new DuplicatedAddress();
        }

        member.passwordencode(passwordEncoder.encode(member.getPassword()));
        return memberRepository.save(member);
    }
}
