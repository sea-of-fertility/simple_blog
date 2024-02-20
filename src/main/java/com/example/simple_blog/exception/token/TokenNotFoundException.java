package com.example.simple_blog.exception.token;

public  class TokenNotFoundException extends TokenException{
    private final static String MESSAGE = "토큰이 존재하지 않습니다.";
    public TokenNotFoundException() {
        super(MESSAGE);
    }

    public TokenNotFoundException(String message) {
        super(message);
    }

    public TokenNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
