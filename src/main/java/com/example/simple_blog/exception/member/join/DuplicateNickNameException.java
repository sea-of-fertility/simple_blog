package com.example.simple_blog.exception.member.join;

public class DuplicateNickNameException extends DuplicateException {

    private final static String MESSAGE = "중복된 닉네임입니다.";

    public DuplicateNickNameException() {
        super(MESSAGE);
    }

    public DuplicateNickNameException(String message) {
        super(message);
    }

    public DuplicateNickNameException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 409;
    }
}
