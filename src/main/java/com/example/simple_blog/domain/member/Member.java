package com.example.simple_blog.domain.member;


import com.example.simple_blog.domain.post.Post;
import jakarta.persistence.*;
import jakarta.transaction.Transactional;
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
    private String password;

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private final LocalDate joinTime = LocalDate.now();

    private String role;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "member")
    private List<Post> posts = new ArrayList<>();

    public void passwordEncode(String encodedPassword) {
        this.password = encodedPassword;
    }

    public Member passwordChange(String newPassword) {
        this.password = newPassword;
        return this;
    }

    @Builder
    public Member(String address, String memberNickName, String memberName, String password, String role, List<Post> posts) {
        this.address = address;
        this.memberNickName = memberNickName;
        this.memberName = memberName;
        this.password = password;
        this.role = role != null? role: "ROLE_USER";
        this.posts = posts != null ? posts: new ArrayList<>();
    }

}
