package com.example.simple_blog.exception.token;

public class RefreshTokenInvalidException extends TokenException{
    private final static String MESSAGE = "invalid refresh token";

    public RefreshTokenInvalidException() {
        super(MESSAGE);
    }

    public RefreshTokenInvalidException(String message) {
        super(message);
    }

    public RefreshTokenInvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
