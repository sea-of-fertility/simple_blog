package com.example.simple_blog.service.post.file;


import com.example.simple_blog.config.properties.StorageProperties;
import com.example.simple_blog.domain.member.Member;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.domain.post.Post;
import com.example.simple_blog.exception.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FileSystemStorageService   {

    private final FileService fileService;
    private final Path rootLocation;


    @Autowired
    public FileSystemStorageService(FileService fileService, StorageProperties properties) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }
        this.fileService = fileService;
        this.rootLocation = Paths.get(properties.getLocation());

    }

    public FilePath store(MultipartFile file, String address, Post post) {
        log.info("StorageService.store {}", file.getName());
        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        Path postDir = this.rootLocation.resolve(address).resolve(String.valueOf(post.getId()));
        this.init(postDir);
        String uuid = UUID.randomUUID().toString();
        String fileExtension = fileExtensionExtractor(file.getOriginalFilename());
        Path destinationFile = postDir.resolve(
                        Paths.get(uuid + "." +fileExtension))
                .normalize().toAbsolutePath();



        if (!destinationFile.getParent().equals(postDir.toAbsolutePath())) {
            // This is a security check
            throw new StorageException(
                    "Cannot store file outside current directory.");
        }
        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile,
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FilePath saveFile = FilePath.builder()
                .fileType(fileExtension)
                .uuid(uuid)
                .filePath(String.valueOf(destinationFile))
                .post(post)
                .build();

        return fileService.save(saveFile);
    }


    public List<String> load(Long postId) {
        if (fileService.existsByPostId(postId)) {
            List<FilePath> byPostId = fileService.findByPostId(postId);

            log.info("fileSystemStorage load{}", byPostId);
            return byPostId.stream()
                    .map(FilePath::getFilePath)
                    .toList();
        }
        return new ArrayList<>();
    }

    public void delete(Post post) {
        log.info("storageService delete start");

        Member member = post.getMember();
        Path postDir = this.rootLocation.resolve(member.getAddress()).resolve(String.valueOf(post.getId()));
        FileSystemUtils.deleteRecursively(postDir.toFile());


    }

    public void deleteAll()
    {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    public void init() {
        try {
            Files.createDirectories(rootLocation);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


    public void init(Path path) {
        try {
            Files.createDirectories(path);
        }
        catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }


    public String fileExtensionExtractor(String fileName){

        Pattern pattern = Pattern.compile("\\.(\\w+)$");
        Matcher matcher = pattern.matcher(fileName);

        // 매칭된 확장자가 있는지 확인하고 추출
        if (matcher.find()) {
            return matcher.group(1); // 첫 번째 그룹이 확장자
        } else {
            return ""; // 매칭된 확장자가 없으면 빈 문자열 반환
        }
    }
}
