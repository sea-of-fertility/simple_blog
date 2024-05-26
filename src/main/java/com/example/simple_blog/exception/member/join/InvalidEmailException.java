package com.example.simple_blog.exception.member.join;

public class InvalidEmailException extends DuplicateException {

    private final static String MESSAGE = "유효하지 않는 이메일 형식입니다.";

    public InvalidEmailException() {
        super(MESSAGE);
    }

    public InvalidEmailException(String MESSAGE) {
        super(MESSAGE);
    }

    public InvalidEmailException(String MESSAGE, Throwable cause) {
        super(MESSAGE, cause);
    }

    @Override
    public int statusCode() {
        return 409;
    }
}
