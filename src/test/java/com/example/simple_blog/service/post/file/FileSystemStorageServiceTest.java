package com.example.simple_blog.service.post.file;

import com.example.simple_blog.config.properties.StorageProperties;
import com.example.simple_blog.domain.post.FilePath;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.IntStream;


@SpringBootTest
class FileSystemStorageServiceTest {


    @Autowired
    StorageService storageService;

    @Autowired
    StorageProperties storageProperties;


    String testEmail = "test@email.com";

    @AfterEach
    void setDirectory() {
        Path path = Paths.get(storageProperties.getLocation());
        path = path.resolve(testEmail);
        File file = new File(String.valueOf(path));
        Arrays.stream(Objects.requireNonNull(file.listFiles())).forEach(File::delete);
        storageService.deleteAll();
    }


    MockMultipartFile multipartFile = new MockMultipartFile("file", "test.txt",
            "text/plain", "Spring Framework".getBytes());


    @Test
    @DisplayName("파일 저장")
    public void fileSave() throws Exception {

        IntStream.range(0, 10)
                .forEach(i -> storageService.store(multipartFile, testEmail));

        Path path = Paths.get(storageProperties.getLocation());
        path = path.resolve(testEmail);
        File file = new File(String.valueOf(path));
        File[] files = file.listFiles();

        assert files != null;
        Assertions.assertThat(Arrays.stream(files).count()).isEqualTo(10);

    }


}