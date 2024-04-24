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

    private String filePath;

    private String fileType;

    private String uuid;


    @Builder
    public FilePath(String filePath, String fileType, String uuid) {
        this.filePath = filePath;
        this.fileType = fileType;
        this.uuid = uuid;
    }
}
