package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public class InvalidPasswordException extends JoinException {

    private final static String MESSAGE = "영어와 숫자, 특수 문자를 사용해";

    public InvalidPasswordException() {
        super(MESSAGE);
    }

    public InvalidPasswordException(String message) {
        super(message);
    }

    public InvalidPasswordException(String message, Throwable cause) {
        super(message, cause);
    }
}
