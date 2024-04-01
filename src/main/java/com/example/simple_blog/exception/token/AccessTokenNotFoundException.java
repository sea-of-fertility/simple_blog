package com.example.simple_blog.exception.token;

public class AccessTokenNotFoundException extends TokenException{

    private final static String MESSAGE = "Not Found Assess Token!";

    public AccessTokenNotFoundException() {
        super(MESSAGE);
    }

    @Override
    public int statusCode() {
        return 400;
    }
}
