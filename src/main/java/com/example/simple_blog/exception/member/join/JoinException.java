package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public abstract class JoinException extends MemberException {
    public JoinException() {
        super();
    }

    public JoinException(String message) {
        super(message);
    }

    public JoinException(String message, Throwable cause) {
        super(message, cause);
    }
}
