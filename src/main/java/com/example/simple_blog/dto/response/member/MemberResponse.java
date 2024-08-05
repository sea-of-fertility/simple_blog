package com.example.simple_blog.dto.response.member;

import lombok.Builder;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;

@Getter
public class MemberResponse  extends RepresentationModel<MemberResponse> {

    private String address;

    private String memberNickName;

    @Builder
    public MemberResponse(String address, String memberNickName) {
        this.address = address;
        this.memberNickName = memberNickName;
    }
}
