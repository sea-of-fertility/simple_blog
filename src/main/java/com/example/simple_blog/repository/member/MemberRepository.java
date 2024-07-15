package com.example.simple_blog.repository.member;

import com.example.simple_blog.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByAddress(String address);

    Boolean existsByAddress(String address);

    Boolean existsByMemberNickName(String nickName);
}
