package com.example.simple_blog.dto.request.member;


import com.example.simple_blog.domain.member.Member;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;

import static com.example.simple_blog.service.member.JoinValidator.*;
@Getter
public class JoinDTO {


    @NotBlank
    @Pattern(regexp = EMAIL_PATTERN, message = "이메일 형식에 맞게 설졍해주세요")
    private String address;

    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(regexp = NICKNAME_PATTERN, message = "길이는 1~8, 특수문자는 사용하지 못합니다.")
    private String memberNickName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "특수문자는 사용할 수 없습니다.")
    private String memberName;

    @NotBlank
    @Pattern(regexp = PASSWORD_PATTERN, message = "형식에 맞게 설졍해주세요")
    @Size(min = 8, max = 13)
    private String password;


    @Builder
    public JoinDTO(String address, String memberNickName, String memberName, String password) {
        this.address = address;
        this.memberNickName = memberNickName;
        this.memberName = memberName;
        this.password = password;
    }


    public Member toEntity() {
        return Member.builder()
                .address(this.address)
                .memberName(this.memberName)
                .memberNickName(this.memberNickName)
                .password(this.password)
                .build();
    }
}
