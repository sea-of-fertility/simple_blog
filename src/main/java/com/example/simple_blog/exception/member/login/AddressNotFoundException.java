package com.example.simple_blog.exception.member.login;

public class AddressNotFoundException extends LoginException {

    private final static String MESSAGE = "가입되지 않은 이메일입니다.";

    public AddressNotFoundException() {
        super(MESSAGE);
    }

    public AddressNotFoundException(String explanation) {
        super(explanation);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
