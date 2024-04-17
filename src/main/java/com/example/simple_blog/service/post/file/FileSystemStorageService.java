package com.example.simple_blog.service.post.file;


import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements  StorageService{

    @Override
    public void init() {

    }

    @Override
    public Path store(BufferedImage file, String charaId) {
        return null;
    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public Resource loadAsResource(String filename) {
        return null;
    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void deleteAll() {

    }
}
