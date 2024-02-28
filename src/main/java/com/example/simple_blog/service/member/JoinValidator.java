package com.example.simple_blog.service.member;

import java.util.regex.Pattern;

public class JoinValidator {

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String NICKNAME_PATTERN = "^[a-zA-Z가-힣]*$";

    private static final Pattern emailPattern = Pattern.compile(EMAIL_PATTERN);
    private static final Pattern nicknamePattern = Pattern.compile(NICKNAME_PATTERN);


    public static boolean isValidEmail(String email) {
        return emailPattern.matcher(email).matches();
    }

    public static boolean isValidNickName(String nickName) {
        return nicknamePattern.matcher(nickName).matches();
    }
}
