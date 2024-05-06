package com.example.simple_blog.exception.post;

public class UnauthorizedDeletionException extends PostException{

    private final static String MESSAGE = "권한이 없는 사용자입니다.";

    public UnauthorizedDeletionException() {
        super();
    }

    public UnauthorizedDeletionException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 403;
    }
}
