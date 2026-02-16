package com.example.Blogging_platform2.exception;
public class PostViewNotFoundException extends RuntimeException {

    public PostViewNotFoundException(Long id) {
        super("PostView with ID " + id + " not found");
    }

    public PostViewNotFoundException(String message) {
        super(message);
    }
}
