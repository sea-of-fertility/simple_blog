package com.example.simple_blog.exception.storage;

public abstract  class FileException extends RuntimeException{

    public abstract int statusCode();

    public FileException() {
        super();
    }

    public FileException(String message) {
        super(message);
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }
}
