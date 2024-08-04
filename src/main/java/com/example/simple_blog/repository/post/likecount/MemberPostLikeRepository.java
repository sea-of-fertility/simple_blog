package com.example.simple_blog.repository.post.likecount;

import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.MemberPostLike;
import com.example.simple_blog.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberPostLikeRepository extends JpaRepository<MemberPostLike, Long>, MemberPostLikeRepositoryCustom {

    void deleteByMemberAndPost(Member member, Post post);

    MemberPostLike findByMemberAndPost(Member member, Post post);
}
