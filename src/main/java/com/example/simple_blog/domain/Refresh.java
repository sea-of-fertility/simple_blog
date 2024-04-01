package com.example.simple_blog.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    private String userAddress;

    private String refresh;

    private Long expired;

    @Builder
    public Refresh(String userAddress, String refresh, Long expired) {
        this.userAddress = userAddress;
        this.refresh = refresh;
        this.expired = expired;
    }
}
