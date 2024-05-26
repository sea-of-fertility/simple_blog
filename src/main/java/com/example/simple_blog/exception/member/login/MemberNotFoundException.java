package com.example.simple_blog.exception.member.login;

import com.example.simple_blog.exception.member.MemberException;

import javax.naming.AuthenticationException;

public class MemberNotFoundException extends LoginException {

    private final static String MESSAGE = "가입되지 않은 사용자압니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
