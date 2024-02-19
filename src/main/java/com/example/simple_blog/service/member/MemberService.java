package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.DuplicatedAddress;
import com.example.simple_blog.exception.member.InvalidEmailException;
import com.example.simple_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder passwordEncoder;





    public Member save(Member member) {
        String address = member.getAddress();

        if (EmailValidator.isValidEmail(address) && !memberRepository.existsByAddress(address)){
            member.setPassword(passwordEncoder.encode(member.getPassword()));
            return memberRepository.save(member);
        } else if (!EmailValidator.isValidEmail(address)) {
            throw new InvalidEmailException();
        }
        else
            throw new DuplicatedAddress();
    }
}
