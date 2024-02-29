package com.example.simple_blog.request.member;


import com.example.simple_blog.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.example.simple_blog.service.member.JoinValidator.*;

public class  JoinDto {

    @NotBlank
    @Pattern(regexp = EMAIL_PATTERN, message = "이메일 형식에 맞게 설졍해주세요")
    private final String address;

    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(regexp = NICKNAME_PATTERN, message = "길이는 1~8, 특수문자는 사용하지 못합니다.")
    private final String memberNickName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "특수문자는 사용할 수 없습니다.")
    private final String memberName;

    @NotBlank
    @Pattern(regexp = PASSWORD_PATTERN, message = "형식에 맞게 설졍해주세요")
    @Size(min = 8, max = 13)
    private final String password;


    @Builder
    public JoinDto(String address, String memberNickName, String memberName, String password) {
        this.address = address;
        this.memberNickName = memberNickName;
        this.memberName = memberName;
        this.password = password;
    }


    public Member toEntity(PasswordEncoder passwordEncoder) {
        return Member.builder()
                .address(this.address)
                .memberName(this.memberName)
                .memberNickName(this.memberNickName)
                .password(passwordEncoder.encode(this.password))
                .build();
    }
}
