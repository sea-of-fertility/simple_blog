package com.example.simple_blog.exception.member.join;

public class DuplicatedEmailException extends DuplicateException {

    private static final String MESSAGE = "중복된 이메일입니다.";

    public DuplicatedEmailException() {
        super(MESSAGE);
    }

    public DuplicatedEmailException(String message) {
        super(message);
    }

    public DuplicatedEmailException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 409;
    }
}
