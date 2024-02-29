package com.example.simple_blog.domain.member;


import com.example.simple_blog.domain.post.Post;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String address;

    @Column(nullable = false, unique = true)
    private String memberNickName;

    @Column(nullable = false)
    private String memberName;

    @Column(nullable = false)
    @Size(min = 8, max = 13, message = "길이는 8 ~ 13 사이로 해주세요")
    private String password;

    @Temporal(value = TemporalType.DATE)
    private final LocalDate joinTime = LocalDate.now();

    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    public void passwordencode(String encodedPassword) {
        this.password = encodedPassword;
    }

    public void passwordChange(String newPassword) {
        this.password = newPassword;
    }

    @Builder
    public Member(String address, String memberNickName, String memberName, String password, String role, List<Post> posts) {
        this.address = address;
        this.memberNickName = memberNickName;
        this.memberName = memberName;
        this.password = password;
        this.role = role != null? role: "ROLE_USER";
        this.posts = posts;
    }

}
