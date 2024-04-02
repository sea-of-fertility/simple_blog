package com.example.simple_blog.exception.storage;

public class StorageException extends RuntimeException{

    private final static String MESSAGE = "Failed to store empty file.";

    public StorageException() {
        super(MESSAGE);
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(Throwable cause) {
        super(MESSAGE,cause);
    }
}
