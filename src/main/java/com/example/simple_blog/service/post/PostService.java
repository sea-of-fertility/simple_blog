package com.example.simple_blog.service.post;

import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.post.PostNotFoundException;
import com.example.simple_blog.repository.PostRepository;
import com.example.simple_blog.service.post.file.FileSystemStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostService {

    private final FileSystemStorageService fileSystemStorageService;
    private final PostRepository postRepository;

    public Post save(Post post){
        return postRepository.save(post);
    }

    public Post get(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public void delete(Post post) {
        fileSystemStorageService.delete(post);
        postRepository.deleteById(post.getId());

    }

    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(PostNotFoundException::new);
    }

    public Page<Post> getPosts(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    @Transactional
    public Post edit(Long id, Post edite) {
        Post post = postRepository.findById(id).orElseThrow(PostNotFoundException::new);
        post.edit(edite);
        return post;
    }
}
