package com.example.simple_blog.exception.member.login;

import javax.security.auth.login.LoginException;

public class LoginFailedException extends RuntimeException {

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
}
