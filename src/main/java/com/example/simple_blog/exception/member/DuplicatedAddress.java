package com.example.simple_blog.exception.member;

public class DuplicatedAddress extends MemberException{

    private static final String MESSAGE = "중복된 이메일입니다.";

    public DuplicatedAddress() {
        super(MESSAGE);
    }

    public DuplicatedAddress(String message) {
        super(message);
    }

    public DuplicatedAddress(String message, Throwable cause) {
        super(message, cause);
    }
}
