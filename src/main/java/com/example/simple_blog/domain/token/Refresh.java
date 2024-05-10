package com.example.simple_blog.domain.token;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Refresh {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String memberAddress;

    @Column(nullable = false)
    private String refresh;

    @Column(nullable = false)
    private Long expired;

    @Builder
    public Refresh(String memberAddress, String refresh, Long expired) {
        this.memberAddress = memberAddress;
        this.refresh = refresh;
        this.expired = expired;
    }
}
