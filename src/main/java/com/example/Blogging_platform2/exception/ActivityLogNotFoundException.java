package com.example.Blogging_platform2.exception;
public class ActivityLogNotFoundException extends RuntimeException {

    public ActivityLogNotFoundException(Long id) {
        super("ActivityLog with ID " + id + " not found");
    }

    public ActivityLogNotFoundException(String message) {
        super(message);
    }
}
