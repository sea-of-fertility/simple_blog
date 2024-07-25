package com.example.simple_blog.repository.post;


import com.example.simple_blog.domain.post.Post;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.simple_blog.domain.member.QMember.member;
import static com.example.simple_blog.domain.post.QPost.post;

@Slf4j
@Repository
@RequiredArgsConstructor
public class PostRepositoryCustomImpl implements PostRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;


    @Override
    public List<Post> getPosts(Long lastIndex) {
        return jpaQueryFactory
                .selectFrom(post)
                .join(post.member, member)
                .where(post.member.eq(member)
                , post.id.loe(lastIndex))
                .orderBy(post.id.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Long findLatestPostIdByMemberId(Long memberId) {
        return jpaQueryFactory
                .select(post.id)
                .from(post)
                .join(post.member, member)
                .where(member.id.eq(memberId))
                .orderBy(post.id.desc())
                .limit(1)
                .fetchOne();
    }


}
