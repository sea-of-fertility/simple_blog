package com.example.simple_blog.service.member;

import java.util.regex.Pattern;

public class JoinValidator {

    public static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    public static final String NICKNAME_PATTERN = "^[a-zA-Z가-힣]*$";
    public static final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*\\W).{8,16}$";


    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern nicknamePattern = Pattern.compile(NICKNAME_PATTERN);
    private static final Pattern passwordPattern = Pattern.compile(PASSWORD_PATTERN);


    public static boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public static boolean isValidNickName(String nickName) {
        return nicknamePattern.matcher(nickName).matches();
    }

    public static boolean isValidPassword(String password) {
        return passwordPattern.matcher(password).matches();
    }
}
