package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.PostDto;
import com.example.Blogging_platform2.exception.PostNotFoundException;
import com.example.Blogging_platform2.model.Post;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@Tag(name = "Post Management", description = "APIs for managing blog posts (create, read, update, delete, search)")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping
    @Operation(summary = "Get all posts with pagination and sorting")
    public ResponseEntity<ApiResponse<Page<PostDto>>> getAllPosts(
            @Parameter(description = "Page number (starts from 0)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of posts per page") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field: createdAt, title, or updatedAt") @RequestParam(defaultValue = "createdAt") String sort) {

        Page<PostDto> postDtos = postService.getAllPosts(page, size, sort)
                .map(this::convertToDto);

        return ResponseEntity.ok(ApiResponse.success("Retrieved posts", postDtos));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get post by ID")
    public ResponseEntity<ApiResponse<PostDto>> getPostById(@PathVariable Long id) {
        Post post = postService.getPostById(id); // no Optional here
        return ResponseEntity.ok(ApiResponse.success("Post retrieved successfully", convertToDto(post)));
    }


    @PostMapping
    @Operation(summary = "Create a new post")
    public ResponseEntity<ApiResponse<PostDto>> createPost(@Valid @RequestBody PostDto postDto) {
        Post post = convertToEntity(postDto);

        // JPA expects entity references instead of raw IDs
        User user = new User();
        user.setId(postDto.getUserId());
        post.setUser(user);

        Post createdPost = postService.savePost(post);
        return new ResponseEntity<>(
                ApiResponse.success("Post created successfully", convertToDto(createdPost)),
                HttpStatus.CREATED
        );
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a post")
    public ResponseEntity<ApiResponse<PostDto>> updatePost(@PathVariable Long id,
                                                           @Valid @RequestBody PostDto postDto,
                                                           @RequestParam Long userId) {
        Post post = convertToEntity(postDto);
        post.setId(id);

        User user = new User();
        user.setId(userId);
        post.setUser(user);

        Post updatedPost = postService.updatePost(id, post);
        return ResponseEntity.ok(ApiResponse.success("Post updated successfully", convertToDto(updatedPost)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a post")
    public ResponseEntity<ApiResponse<Void>> deletePost(@PathVariable Long id,
                                                        @RequestParam Long userId) {
        postService.deletePost(id);
        return ResponseEntity.ok(ApiResponse.success("Post deleted successfully"));
    }

    @GetMapping("/search")
    @Operation(summary = "Search posts by title keyword")
    public ResponseEntity<ApiResponse<Page<PostDto>>> searchPosts(@RequestParam String query,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostDto> postDtos = postService.searchPostsByTitle(query, pageable)
                .map(this::convertToDto);

        return ResponseEntity.ok(ApiResponse.success("Found " + postDtos.getTotalElements() + " posts", postDtos));
    }

    // Helper methods
    private PostDto convertToDto(Post post) {
        return new PostDto(
                post.getId(),
                post.getUser().getId(),
                post.getTitle(),
                post.getContent(),
                post.getCreatedAt(),
                post.getUpdatedAt()
        );
    }

    private Post convertToEntity(PostDto postDto) {
        Post post = new Post();
        post.setTitle(postDto.getTitle());
        post.setContent(postDto.getContent());
        return post;
    }
}
