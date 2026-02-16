package com.example.Blogging_platform2.exception;
public class TagNotFoundException extends RuntimeException {
    public TagNotFoundException(Long id) {
        super("Tag with ID " + id + " not found");
    }

    public TagNotFoundException(String message) {
        super(message);
    }
}
