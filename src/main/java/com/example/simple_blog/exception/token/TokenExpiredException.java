package com.example.simple_blog.exception.token;

public class TokenExpiredException extends TokenException{

    private static final String MESSAGE = "만료된 토큰입니다.";

    public TokenExpiredException() {
        super();
    }

    public TokenExpiredException(String message) {
        super(message);
    }

    public TokenExpiredException(String message, Throwable cause) {
        super(message, cause);
    }
}
