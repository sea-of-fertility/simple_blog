package com.example.simple_blog.config;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.exception.member.login.AddressNotFoundException;
import com.example.simple_blog.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String address) throws UsernameNotFoundException {

        Member member = memberRepository.findByAddress(address)
                .orElseThrow(AddressNotFoundException::new);

        return new MemberDetail(member);
    }
}
