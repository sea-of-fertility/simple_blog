package com.example.simple_blog.domain.post;

import com.example.simple_blog.domain.member.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FilePath> imagePath = new ArrayList<>();

    @Temporal(value = TemporalType.TIME)
    private final LocalTime createTime = LocalTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Member member;

    @Builder
    public Post(String title, List<FilePath> imagePath, String content) {
        this.title = title;
        this.imagePath = imagePath;
        this.content = content;
    }
}
