package com.example.simple_blog.service.member;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.join.*;
import com.example.simple_blog.exception.member.login.MemberNotFoundException;
import com.example.simple_blog.repository.MemberRepository;
import jakarta.transaction.Transactional;
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

    public Member findByMemberId(Long id) throws MemberNotFoundException {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }


    public Member findByAddress(String address) throws MemberNotFoundException {
        return memberRepository.findByAddress(address).orElseThrow(MemberNotFoundException::new);
    }

    public Member save(Member member) {

        try {
            checkJoin(member);
            String encode = passwordEncoder.encode(member.getPassword());
            member.passwordEncode(encode);
            return memberRepository.save(member);

        } catch (JoinException e) {
            log.info("잘못된 형식의 가입시도 {}", e.getMessage());
            throw e;
        }
            }

    public void delete(Member member, String password) {
        if (passwordEncoder.matches(password, member.getPassword())) {
            memberRepository.delete(member);
        } else {
            throw new InvalidPasswordException("정확한 현재 비밀번호를 입력해 주세요.");
        }
    }


    @Transactional
    public void passwordChange(Member member, String beforePWD, String afterPWD) {
        if (!passwordEncoder.matches(beforePWD, member.getPassword())){
            throw new InvalidPasswordException("현재 비밀번호를 잘못 입력하셨습니다.");
        }

        if (!JoinValidator.isValidPassword(afterPWD))
            throw new InvalidPasswordException();

        String encode = passwordEncoder.encode(afterPWD);
        log.info("{}", encode);
        Member member1 = member.passwordChange(encode);
        memberRepository.save(member1);
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
