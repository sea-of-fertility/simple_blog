package com.example.simple_blog.exception.member.login;

import com.example.simple_blog.exception.member.MemberException;

public class AddressNotFoundException extends LoginException {

    public AddressNotFoundException() {
        super();
    }

    public AddressNotFoundException(String message) {
        super(message);
    }

    public AddressNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
