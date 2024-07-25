package com.example.simple_blog.exception.post.comment;

import com.example.simple_blog.exception.post.PostException;

public class CommentNotFoundException extends PostException {

    private final static String MESSAGE ="댓글이 존재하지 않습니다.";

    public CommentNotFoundException() {
        super(MESSAGE);
    }

    public CommentNotFoundException(String s) {
        super(s);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
