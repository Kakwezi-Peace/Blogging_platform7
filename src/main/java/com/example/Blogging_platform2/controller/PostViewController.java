package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.PostViewDto;
import com.example.Blogging_platform2.exception.PostViewNotFoundException;
import com.example.Blogging_platform2.model.Post;
import com.example.Blogging_platform2.model.PostView;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.PostViewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/views")
@Tag(name = "Post View Management", description = "APIs for tracking post views")
@RequiredArgsConstructor
public class PostViewController {

    private final PostViewService service;

    @PostMapping
    @Operation(summary = "Log a new post view")
    public ResponseEntity<ApiResponse<PostViewDto>> createView(@Valid @RequestBody PostViewDto dto) {
        PostView view = new PostView();

        Post post = new Post();
        post.setId(dto.getPostId());
        view.setPost(post);

        User user = new User();
        user.setId(dto.getUserId());
        view.setUser(user);

        view.setViewedAt(dto.getViewedAt());

        PostView created = service.savePostView(view);
        PostViewDto responseDto = convertToDto(created);

        return new ResponseEntity<>(
                ApiResponse.success("View logged successfully", responseDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/post/{postId}")
    @Operation(summary = "Get all views for a post")
    public ResponseEntity<ApiResponse<List<PostViewDto>>> getViewsByPost(@PathVariable Long postId) {
        List<PostViewDto> views = service.getViewsByPost(postId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Retrieved " + views.size() + " views", views));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get a view by ID")
    public ResponseEntity<ApiResponse<PostViewDto>> getView(@PathVariable Long id) {
        PostView view = service.getPostViewById(id)
                .orElseThrow(() -> new PostViewNotFoundException("View with ID " + id + " not found"));

        return ResponseEntity.ok(ApiResponse.success("View retrieved successfully", convertToDto(view)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a view by ID")
    public ResponseEntity<ApiResponse<Void>> deleteView(@PathVariable Long id) {
        if (service.getPostViewById(id).isEmpty()) {
            throw new PostViewNotFoundException("View with ID " + id + " not found");
        }
        service.deletePostView(id);
        return ResponseEntity.ok(ApiResponse.success("View deleted successfully"));
    }

    private PostViewDto convertToDto(PostView view) {
        PostViewDto dto = new PostViewDto();
        dto.setId(view.getId());
        dto.setPostId(view.getPost().getId());
        dto.setUserId(view.getUser().getId());
        dto.setViewedAt(view.getViewedAt());
        return dto;
    }
}
