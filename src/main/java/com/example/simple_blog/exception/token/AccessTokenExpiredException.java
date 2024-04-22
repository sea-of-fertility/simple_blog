package com.example.simple_blog.exception.token;

public class AccessTokenExpiredException extends TokenException{

    private static final String MESSAGE = "Expired Token";

    public AccessTokenExpiredException() {
        super(MESSAGE);
    }

    public AccessTokenExpiredException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 401;
    }
}
