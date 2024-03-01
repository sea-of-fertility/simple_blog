package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.join.*;
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
            log.info("checkJoin 전{}", member.getPassword());
            checkJoin(member);
            log.info("checkJoin 후{}", member.getPassword());
            String encode = passwordEncoder.encode(member.getPassword());
            member.passwordEncode(encode);
            return memberRepository.save(member);

        } catch (JoinException e) {
            log.info("잘못된 형식의 가입시도 {}", e.getMessage());
            throw e;
        }
            }

    public void passwordChange(Member member, String newPassword) {
        if (!JoinValidator.isValidPassword(newPassword))
            throw new InvalidPasswordException();

        String encode = passwordEncoder.encode(newPassword);
        log.info("{}", encode);
        member.passwordChange(encode);
    }

    public void checkJoin(Member member) {
        if (memberRepository.existsByAddress(member.getAddress())) {
            throw new DuplicatedAddress();
        }
        else if (!JoinValidator.isValidEmail(member.getAddress())) {
            throw new InvalidEmailException();
        }
        else if (memberRepository.existsByMemberNickName(member.getMemberNickName())){
            throw new DuplicateNickName();
        }
        else if(!JoinValidator.isValidNickName(member.getMemberNickName())){
            throw new InvalidNickNameException();
        }
        else if (!JoinValidator.isValidPassword(member.getPassword())) {
            throw new InvalidPasswordException();
        }
    }

}
