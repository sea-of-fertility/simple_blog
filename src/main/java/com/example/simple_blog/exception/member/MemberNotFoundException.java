package com.example.simple_blog.exception.member;

public class MemberNotFoundException extends MemberException{

    private final static String MESSAGE = "가입되지 않은 사용자압니다.";

    public MemberNotFoundException() {
        super(MESSAGE);
    }

    public MemberNotFoundException(String message) {
        super(message);
    }
}
