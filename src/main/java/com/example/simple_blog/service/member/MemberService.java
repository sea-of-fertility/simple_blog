package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.join.*;
import com.example.simple_blog.exception.member.MemberException;
import com.example.simple_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public Member save(Member member) {

        try {
            checkJoin(member);
            return memberRepository.save(member);

        } catch (JoinException e) {
            log.info("잘못된 형식의 가입시도 {}", e.getMessage());
            throw e;
        }
            }

    public void checkJoin(Member member) {
        if (memberRepository.existsByAddress(member.getAddress())) {
            throw new DuplicatedAddress();
        }
        else if (memberRepository.existsByMemberNickName(member.getMemberNickName())){
            throw new DuplicateNickName();
        }
        else if (!JoinValidator.isValidEmail(member.getAddress())) {
            throw new InvalidEmailException();
        } else if (!JoinValidator.isValidPassword(member.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

}
