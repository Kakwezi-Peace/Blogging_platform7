package com.example.Blogging_platform2.exception;
public class PostNotFoundException extends RuntimeException {

    public PostNotFoundException(Long id) {
        super("Post with ID " + id + " not found");
    }

    public PostNotFoundException(String message) {
        super(message);
    }
}
