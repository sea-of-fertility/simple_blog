package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public abstract class DuplicateException extends MemberException {
    public DuplicateException() {
        super();
    }

    public DuplicateException(String message) {
        super(message);
    }

    public DuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();
}
