package com.example.simple_blog.service.post.file;


import com.example.simple_blog.config.properties.StorageProperties;
import com.example.simple_blog.domain.post.FilePath;
import com.example.simple_blog.exception.storage.StorageException;
import com.example.simple_blog.exception.storage.StorageFileNotFoundException;
import com.example.simple_blog.repository.FileRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
@Slf4j
public class FileSystemStorageService implements  StorageService{


    private final Path rootLocation;
    private final FileRepository fileRepository;


    @Autowired
    public FileSystemStorageService(StorageProperties properties, FileRepository fileRepository) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
        this.fileRepository = fileRepository;

    }

    @Override
    public FilePath store(MultipartFile file, String address) {

        if (file.isEmpty()) {
            throw new StorageException("Failed to store empty file.");
        }

        Path memberDir = this.rootLocation.resolve(address);
        init(memberDir);
        String uuid = UUID.randomUUID().toString();

        Path destinationFile = memberDir.resolve(
                        Paths.get(Objects.requireNonNull(uuid)))
                .normalize().toAbsolutePath();

        String fileExtension = fileExtensionExtractor(file.getOriginalFilename());

        if (!destinationFile.getParent().equals(memberDir.toAbsolutePath())) {
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
                .build();

        return fileRepository.save(saveFile);


    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        }
        catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException(
                        "Could not read file: " + filename);

            }
        }
        catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
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
