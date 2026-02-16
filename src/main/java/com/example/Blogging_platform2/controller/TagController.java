package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.TagDto;
import com.example.Blogging_platform2.exception.TagNotFoundException;
import com.example.Blogging_platform2.model.Tag;
import com.example.Blogging_platform2.service.TagService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tags")
@io.swagger.v3.oas.annotations.tags.Tag(name = "Tag Management", description = "APIs for managing tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService service;

    @PostMapping
    @Operation(summary = "Create a new tag")
    public ResponseEntity<ApiResponse<TagDto>> createTag(@Valid @RequestBody TagDto dto) {
        Tag tag = new Tag();
        tag.setName(dto.getName());

        Tag created = service.saveTag(tag);
        TagDto responseDto = convertToDto(created);

        return new ResponseEntity<>(
                ApiResponse.success("Tag created successfully", responseDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all tags")
    public ResponseEntity<ApiResponse<List<TagDto>>> getAllTags() {
        List<TagDto> tags = service.getAllTags().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Retrieved " + tags.size() + " tags", tags));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get tag by ID")
    public ResponseEntity<ApiResponse<TagDto>> getTag(@PathVariable Long id) {
        Tag tag = service.getTagById(id)
                .orElseThrow(() -> new TagNotFoundException("Tag with ID " + id + " not found"));

        return ResponseEntity.ok(ApiResponse.success("Tag retrieved successfully", convertToDto(tag)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete tag by ID")
    public ResponseEntity<ApiResponse<Void>> deleteTag(@PathVariable Long id) {
        if (service.getTagById(id).isEmpty()) {
            throw new TagNotFoundException("Tag with ID " + id + " not found");
        }
        service.deleteTag(id);
        return ResponseEntity.ok(ApiResponse.success("Tag deleted successfully"));
    }

    private TagDto convertToDto(Tag tag) {
        TagDto dto = new TagDto();
        dto.setId(tag.getId());
        dto.setName(tag.getName());
        return dto;
    }
}
