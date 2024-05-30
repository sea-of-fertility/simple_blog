package com.example.simple_blog.domain.post;

import com.example.simple_blog.domain.member.Member;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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

    @Temporal(value = TemporalType.DATE)
    @Column(nullable = false)
    private final LocalDate createTime = LocalDate.now();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private List<FilePath> filePaths = new ArrayList<>();



    @Builder
    public Post(String title, String content, Member member) {
        this.title = title;
        this.content = content;
        this.member = member;
    }


    public void edit(Post post) {
        this.title = post.getTitle();
        this.content = post.getContent();
    }
}
