package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    public void post(Post post) {
        postRepository.save(post);
    }
}
