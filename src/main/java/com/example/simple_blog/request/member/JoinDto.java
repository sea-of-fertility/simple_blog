package com.example.simple_blog.request.member;


import com.example.simple_blog.domain.member.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;

public class  JoinDto {

    @NotBlank
    @Email
    private final String address;

    @NotBlank
    @Size(min = 1, max = 8)
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]*$", message = "특수문자는 사용할 수 없습니다.")
    private final String memberNickName;

    @NotBlank
    @Pattern(regexp = "^[a-zA-Z가-힣]*$", message = "특수문자는 사용할 수 없습니다.")
    private final String memberName;

    @NotBlank
    @Size(min = 8, max = 13)
    private final String password;

    @Builder
    public JoinDto(String address, String memberNickName, String memberName, String password) {
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
