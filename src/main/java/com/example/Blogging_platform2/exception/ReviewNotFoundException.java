package com.example.Blogging_platform2.exception;
public class ReviewNotFoundException extends RuntimeException {
    public ReviewNotFoundException(Long id) {
        super("Review with ID " + id + " not found");
    }

    public ReviewNotFoundException(String message) {
        super(message);
    }
}
