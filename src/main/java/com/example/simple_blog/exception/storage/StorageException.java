package com.example.simple_blog.exception.storage;

public  class StorageException extends FileException{

    @Override
    public int statusCode() {
        return 404;
    }

    public StorageException() {
        super();
    }

    public StorageException(String message) {
        super(message);
    }

    public StorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
