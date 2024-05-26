package com.example.simple_blog.exception.member.login;

public class LoginFailedException extends LoginException {

    private final static String MESSAGE = "Login Failed";

    public LoginFailedException() {
        super(MESSAGE);
    }

    public LoginFailedException(String message) {
        super(message);
    }

    public LoginFailedException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
