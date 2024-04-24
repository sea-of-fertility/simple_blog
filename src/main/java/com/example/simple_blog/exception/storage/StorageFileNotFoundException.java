package com.example.simple_blog.exception.storage;

public class StorageFileNotFoundException extends StorageException{


    public StorageFileNotFoundException() {
        super();
    }

    public StorageFileNotFoundException(String message) {
        super(message);
    }

    public StorageFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int statusCode() {
        return 404;
    }
}
