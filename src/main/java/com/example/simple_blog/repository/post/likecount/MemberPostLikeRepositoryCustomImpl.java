package com.example.simple_blog.repository.post.likecount;

import com.example.simple_blog.domain.post.MemberPostLike;
import com.example.simple_blog.domain.post.QMemberPostLike;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


@RequiredArgsConstructor
@Repository
public class MemberPostLikeRepositoryCustomImpl implements MemberPostLikeRepositoryCustom {


    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public Boolean hasLikePost(MemberPostLike memberPostLike) {
        Integer i = jpaQueryFactory.selectOne()
                .from(QMemberPostLike.memberPostLike)
                .where(QMemberPostLike.memberPostLike.member
                            .eq(memberPostLike.getMember())
                        .and(QMemberPostLike.memberPostLike.post
                            .eq(memberPostLike.getPost())))
                .fetchFirst();
        return i != null;
    }
}
