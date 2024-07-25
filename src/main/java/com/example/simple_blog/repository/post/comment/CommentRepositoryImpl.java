package com.example.simple_blog.repository.post.comment;


import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.domain.post.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.simple_blog.domain.post.QComment.*;


@RequiredArgsConstructor
@Repository
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> getComments(Long postId, Long startIndex) {
        return jpaQueryFactory.selectFrom(comment)
                .leftJoin(comment.parent)
                .fetchJoin()
                .where(comment.post.id.eq(postId)
                        .and(comment.id.goe(startIndex)))
                .orderBy(comment.parent.id.asc().nullsFirst(), comment.id.asc())
                .limit(30L)
                .fetch();
    }


    @Override
    public Long getFirstComment(Long postId) {
        return jpaQueryFactory
                .select(comment.id)
                .from(comment)
                .where(comment.post.id.eq(postId))
                .orderBy(comment.id.asc())
                .limit(1)
                .fetchOne();

    }


}
