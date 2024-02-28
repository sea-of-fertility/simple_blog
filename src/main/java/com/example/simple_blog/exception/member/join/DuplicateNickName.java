package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public class DuplicateNickName extends JoinException {

    private final static String MESSAGE = "중복된 닉네임입니다.";

    public DuplicateNickName() {
        super(MESSAGE);
    }

    public DuplicateNickName(String message) {
        super(message);
    }

    public DuplicateNickName(String message, Throwable cause) {
        super(message, cause);
    }
}
