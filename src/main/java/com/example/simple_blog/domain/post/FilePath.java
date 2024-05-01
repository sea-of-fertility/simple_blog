package com.example.simple_blog.domain.post;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FilePath {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String filePath;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false, unique = true)
    private String uuid;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public FilePath(String filePath, String fileType, String uuid, Post post) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.post = post;
        this.uuid = uuid;
    }
}
