package com.example.simple_blog.service.post.file;

import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    public FilePath save(FilePath filePath) {
        return fileRepository.save(filePath);
    }

    public Boolean existsByPostId(Long id) {
        return fileRepository.existsByPostId(id);

    }

    public List<FilePath> findByPostId(Long id) {
        return fileRepository.findByPostId(id);
    }

    public void delete(FilePath filePath) {
        fileRepository.delete(filePath);
    }

}
