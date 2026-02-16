package com.example.Blogging_platform2.exception;
public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long id) {
        super("Comment with ID " + id + " not found");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
