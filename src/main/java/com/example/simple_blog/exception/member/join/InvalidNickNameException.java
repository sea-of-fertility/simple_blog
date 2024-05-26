package com.example.simple_blog.exception.member.join;

public class InvalidNickNameException extends DuplicateException {

    private static final String MESSAGE = "영어 한글만 가능합니다.";

    public InvalidNickNameException() {
        super(MESSAGE);
    }

    public InvalidNickNameException(String message) {
        super(message);
    }

    public InvalidNickNameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 409;
    }
}
