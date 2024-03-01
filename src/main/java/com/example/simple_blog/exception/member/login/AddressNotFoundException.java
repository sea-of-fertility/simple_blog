package com.example.simple_blog.exception.member.login;

import javax.naming.AuthenticationException;

public class AddressNotFoundException extends AuthenticationException {

    private final static String MESSAGE = "가입되지 않은 이메일입니다.";

    public AddressNotFoundException() {
        super(MESSAGE);
    }

    public AddressNotFoundException(String explanation) {
        super(explanation);
    }
}
