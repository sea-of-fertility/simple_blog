package com.example.simple_blog.domain.post;

import com.example.simple_blog.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty
    private String title;

    @Lob
    @Column(nullable = false)
    @NotEmpty
    private String content;

    @OneToMany
    @JoinColumn(name = "File_Path_Id")
    private final List<FilePath> imagePaths = new ArrayList<>();

    @Temporal(value = TemporalType.TIME)
    @Column(nullable = false)
    private final LocalTime createTime = LocalTime.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    public void saveFilePath(FilePath filePath) {
        imagePaths.add(filePath);
    }

    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }
}
