package com.example.simple_blog.exception.member.join;

import com.example.simple_blog.exception.member.MemberException;

public abstract class InvalidException  extends MemberException {

    public InvalidException() {
        super();
    }

    public InvalidException(String message) {
        super(message);
    }

    public InvalidException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int statusCode();
}
