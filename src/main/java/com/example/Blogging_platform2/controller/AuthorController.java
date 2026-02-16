package com.example.Blogging_platform2.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @PreAuthorize("hasRole('AUTHOR')")
    @PostMapping("/createPost")
    public String createPost() {
        return "Author can create posts!";
    }
}
