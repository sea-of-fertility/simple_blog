package com.example.simple_blog.service.member;

import java.util.regex.Pattern;

public class EmailValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final Pattern pattern = Pattern.compile(EMAIL_PATTERN);

    public static boolean isValidEmail(String email) {
        return pattern.matcher(email).matches();
    }

}
