package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public class InvalidPasswordException extends JoinException {

    private final static String MESSAGE = "비밀번호는 영어 숫자, 특수 문자를 사용해주세요";

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
