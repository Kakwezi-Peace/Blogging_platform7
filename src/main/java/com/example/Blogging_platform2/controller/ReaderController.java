package com.example.Blogging_platform2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reader")
public class ReaderController {

    @PreAuthorize("hasRole('READER')")
    @GetMapping("/viewPosts")
    public String viewPosts() {
        return "Reader can view posts!";
    }
}
