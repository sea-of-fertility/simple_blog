package com.example.simple_blog.service.post;


import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.MemberPostLike;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.post.likecount.MemberPostLikeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class MemberPostLikeService {

    private final MemberPostLikeRepository memberPostLikeRepository;

    public MemberPostLike findByMemberAndPost(Post post, Member member) {
        return memberPostLikeRepository.findByMemberAndPost(member, post);
    }

    @Transactional
    public void clickLikeButton(MemberPostLike memberPostLike) {
        Member member = memberPostLike.getMember();
        Post post = memberPostLike.getPost();
        if (memberPostLikeRepository.hasLikePost(memberPostLike)) {
            post.likeCountDecrement();
            deleteMemberPostLike(post, member);

        } else {
            memberPostLikeRepository.save(memberPostLike);
            post.likeCountIncrement();
        }
    }

    public void deleteMemberPostLike(Post post, Member member) {
        memberPostLikeRepository.deleteByMemberAndPost(member, post);
    }
}
