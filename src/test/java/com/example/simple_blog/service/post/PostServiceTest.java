package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.PostRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class PostServiceTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    private String exampleTitle = "title";
    private String exampleContent = "title";

    @AfterEach
    public void setPostRepository() {
        postRepository.deleteAll();
    }

    @Test
    @DisplayName("게시글 등록")
    public void post() throws Exception {
        //given
        Post post = Post.builder()
                .title(exampleTitle)
                .content(exampleContent)
                .build();

        //when
        postService.post(post);

        //then
        Assertions.assertThat(postRepository.count()).isEqualTo(1);

    }

}