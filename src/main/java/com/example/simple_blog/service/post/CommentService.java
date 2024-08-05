package com.example.simple_blog.service.post;


import com.example.simple_blog.domain.post.Comment;
import com.example.simple_blog.exception.post.comment.CommentNotFoundException;
import com.example.simple_blog.repository.post.comment.CommentRepository;
import com.example.simple_blog.dto.service.post.comment.CommentsDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    public Comment save(Comment comment) {
        exitsComment(comment);
        return commentRepository.save(comment);
    }

    public Comment findByParentId(Long parentId) {
        return commentRepository.findById(parentId).orElseThrow(CommentNotFoundException::new);
    }


    @Transactional(readOnly = true)
    public List<CommentsDTO> getComments(Long postId) {
        Long latestIndex = commentRepository.getFirstComment(postId);
        List<Comment> comments = commentRepository.getComments(postId, latestIndex);
        return sortComments(comments);
    }


    @Transactional(readOnly = true)
    public List<CommentsDTO> getComments(Long postId, Long startIndex) {
        List<Comment> comments = commentRepository.getComments(postId, startIndex);
        return sortComments(comments);
    }


    private void exitsComment(Comment comment) {
        if (comment.getParent() != null && !commentRepository.existsById(comment.getParent().getId())) {
            throw new CommentNotFoundException("존재하지 않는 댓글에 달았습니다.");
        }
    }


    private List<CommentsDTO> sortComments(List<Comment> commentList) {
        List<CommentsDTO> comments = new ArrayList<>();
        Map<Long, CommentsDTO> commentMap = new HashMap<>();

        commentList.forEach(comment -> {
            CommentsDTO commentResponse = CommentsDTO.builder()
                    .parentId(comment.getParent() != null? comment.getParent().getId():null)
                    .content(comment.getContent())
                    .commentId(comment.getId())
                    .postId(comment.getPost().getId())
                    .createTime(comment.getCreateTime())
                    .author(comment.getAuthor().getMemberNickName())
                    .build();

            if (comment.getParent() != null) {
                commentMap.get(comment.getParent().getId()).getChildren().add(commentResponse);
            } else {
                comments.add(commentResponse);
            }

            commentMap.put(comment.getId(), commentResponse);
        });
        return comments;
    }
}
