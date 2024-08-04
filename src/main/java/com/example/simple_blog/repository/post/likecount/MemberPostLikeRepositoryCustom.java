package com.example.simple_blog.repository.post.likecount;

import com.example.simple_blog.domain.post.MemberPostLike;

public interface MemberPostLikeRepositoryCustom {

    Boolean hasLikePost(MemberPostLike memberPostLike);

}
