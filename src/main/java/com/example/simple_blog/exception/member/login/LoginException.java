package com.example.simple_blog.exception.member.login;

public abstract class LoginException extends RuntimeException{

    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }


    public abstract int statusCode();
}
