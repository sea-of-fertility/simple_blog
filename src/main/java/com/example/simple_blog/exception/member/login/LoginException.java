package com.example.simple_blog.exception.member.login;

import com.example.simple_blog.exception.member.MemberException;

import java.util.Map;

public abstract class LoginException extends MemberException {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
