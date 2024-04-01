package com.example.simple_blog.exception.token;

public class RefreshTokenNotFoundException extends TokenException{


    private final static String MESSAGE = "refresh token을 찾지 못했습니다.";
    public RefreshTokenNotFoundException() {
        super();
    }

    public RefreshTokenNotFoundException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
