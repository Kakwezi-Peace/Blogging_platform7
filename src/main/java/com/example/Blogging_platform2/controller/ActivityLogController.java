package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.ActivityLogDto;
import com.example.Blogging_platform2.exception.ActivityLogNotFoundException;
import com.example.Blogging_platform2.model.ActivityLog;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.ActivityLogService;
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
@RequestMapping("/api/logs")
@Tag(name = "Activity Log Management", description = "APIs for tracking user activities")
@RequiredArgsConstructor
public class ActivityLogController {

    private final ActivityLogService service;

    @PostMapping
    @Operation(summary = "Create a new activity log")
    public ResponseEntity<ApiResponse<ActivityLogDto>> createLog(@Valid @RequestBody ActivityLogDto dto) {
        ActivityLog log = new ActivityLog();

        User user = new User();
        user.setId(dto.getUserId());
        log.setUser(user);

        log.setAction(dto.getAction());
        log.setTargetId(dto.getTargetId());
        log.setDetails(dto.getDetails());
        log.setTimestamp(dto.getTimestamp());

        ActivityLog created = service.saveLog(log);
        ActivityLogDto responseDto = convertToDto(created);

        return new ResponseEntity<>(
                ApiResponse.success("Activity logged successfully", responseDto),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get all activity logs")
    public ResponseEntity<ApiResponse<List<ActivityLogDto>>> getAllLogs() {
        List<ActivityLogDto> logs = service.getAllLogs().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success("Retrieved " + logs.size() + " logs", logs));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get activity log by ID")
    public ResponseEntity<ApiResponse<ActivityLogDto>> getLog(@PathVariable Long id) {
        ActivityLog log = service.getLogById(id)
                .orElseThrow(() -> new ActivityLogNotFoundException("Log with ID " + id + " not found"));

        return ResponseEntity.ok(ApiResponse.success("Log retrieved successfully", convertToDto(log)));
    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete activity log by ID")
    public ResponseEntity<ApiResponse<Void>> deleteLog(@PathVariable Long id) {
        if (service.getLogById(id).isEmpty()) {
            throw new ActivityLogNotFoundException("Log with ID " + id + " not found");
        }
        service.deleteLog(id);
        return ResponseEntity.ok(ApiResponse.success("Log deleted successfully"));
    }

    private ActivityLogDto convertToDto(ActivityLog log) {
        ActivityLogDto dto = new ActivityLogDto();
        dto.setId(log.getId());
        dto.setUserId(log.getUser().getId());
        dto.setAction(log.getAction());
        dto.setTargetId(log.getTargetId());
        dto.setDetails(log.getDetails());
        dto.setTimestamp(log.getTimestamp());
        return dto;
    }
}
