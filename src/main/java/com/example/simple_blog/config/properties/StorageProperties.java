package com.example.simple_blog.config.properties;


import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("storage")
@Getter
public class StorageProperties {

    private String location = "upload-dir";

    public void setLocation(String location) {
        this.location = location;
    }
}
