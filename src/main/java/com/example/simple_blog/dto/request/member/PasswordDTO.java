package com.example.simple_blog.dto.request.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PasswordDTO {
    private String password;

    @Builder
    public PasswordDTO(String password) {
        this.password = password;
    }
}
