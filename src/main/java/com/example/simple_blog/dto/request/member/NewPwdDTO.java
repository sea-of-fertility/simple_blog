package com.example.simple_blog.dto.request.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NewPwdDTO {

    private String beforePassword;
    private String afterPassword;

    @Builder
    public NewPwdDTO(String beforePassword, String afterPassword) {
        this.beforePassword = beforePassword;
        this.afterPassword = afterPassword;
    }
}
