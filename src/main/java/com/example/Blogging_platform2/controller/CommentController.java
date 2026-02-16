package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.CommentDto;
import com.example.Blogging_platform2.exception.CommentNotFoundException;
import com.example.Blogging_platform2.model.Comment;
import com.example.Blogging_platform2.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@Tag(name = "Comment Management", description = "APIs for managing comments")
@RequiredArgsConstructor
public class CommentController {


    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "Create a new comment")
    public ResponseEntity<ApiResponse<CommentDto>> createComment(@Valid @RequestBody CommentDto dto) {
        Comment created = commentService.saveComment(dto);
        CommentDto responseDto = convertToDto(created);

        return new ResponseEntity<>(
                ApiResponse.success("Comment created successfully", responseDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<ApiResponse<List<CommentDto>>> getCommentsByPost(@PathVariable Long postId) {
        List<Comment> comments = commentService.getCommentsByPost(postId);
        List<CommentDto> dtos = comments.stream()
                .map(this::convertToDto)
                .toList();

        return ResponseEntity.ok(
                ApiResponse.success("Retrieved " + dtos.size() + " comments", dtos)
        );
    }


    @GetMapping("/{id}")
    @Operation(summary = "Get comment by ID")
    public ResponseEntity<ApiResponse<CommentDto>> getComment(@PathVariable Long id) {
        Comment comment = commentService.getCommentById(id);
        if (comment == null) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
        return ResponseEntity.ok(ApiResponse.success("Comment retrieved successfully", convertToDto(comment)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete comment by ID")
    public ResponseEntity<ApiResponse<Void>> deleteComment(@PathVariable Long id) {
        if (commentService.getCommentById(id) == null) {
            throw new CommentNotFoundException("Comment with ID " + id + " not found");
        }
        commentService.deleteComment(id);
        return ResponseEntity.ok(ApiResponse.success("Comment deleted successfully"));
    }

    private CommentDto convertToDto(Comment comment) {
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setPostId(comment.getPost().getId());
        dto.setUserId(comment.getUser().getId());
        dto.setContent(comment.getContent());
        dto.setCreatedAt(comment.getCreatedAt());
        return dto;
    }
}
