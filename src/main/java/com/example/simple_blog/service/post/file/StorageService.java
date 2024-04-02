package com.example.simple_blog.service.post.file;
import org.springframework.core.io.Resource;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {
    void init();

    Path store(BufferedImage file, String charaId);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void delete(String id);

    void deleteAll();

}
