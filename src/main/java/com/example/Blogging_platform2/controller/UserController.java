package com.example.Blogging_platform2.controller;

import com.example.Blogging_platform2.dto.ApiResponse;
import com.example.Blogging_platform2.dto.UserDto;
import com.example.Blogging_platform2.exception.UserNotFoundException;
import com.example.Blogging_platform2.model.Role;
import com.example.Blogging_platform2.model.User;
import com.example.Blogging_platform2.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
@Tag(name = "User Management", description = "APIs for managing users (registration, retrieval, updates, deletion)")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account. Username and email must be unique.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "User registered successfully"
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Validation error"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Username or email already exists")
    })
    public ResponseEntity<ApiResponse<UserDto>> registerUser(@Valid @RequestBody UserDto userDto) {
        User user = new User();
        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());

        // Map String role from DTO to Role entity
        Role defaultRole = new Role();
        defaultRole.setName(userDto.getRole() != null ? userDto.getRole() : "ROLE_USER");
        user.setRoles(Set.of(defaultRole));

        User createdUser = userService.registerUser(user);
        UserDto responseDto = convertToDto(createdUser);

        return new ResponseEntity<>(ApiResponse.success("User registered successfully", responseDto), HttpStatus.CREATED);
    }

    @GetMapping("/{username}")
    @Operation(summary = "Get user by username", description = "Retrieves a user's information by their username")
    public ResponseEntity<ApiResponse<UserDto>> getUserByUsername(@PathVariable String username) {
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User with username '" + username + "' not found"));
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", convertToDto(user)));
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves a list of all registered users")
    public ResponseEntity<ApiResponse<List<UserDto>>> getAllUsers() {
        List<UserDto> userDtos = userService.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success("Retrieved " + userDtos.size() + " users", userDtos));
    }

    @GetMapping("/id/{id}")
    @Operation(summary = "Get user by ID", description = "Retrieves a user's information by their ID")
    public ResponseEntity<ApiResponse<UserDto>> getUserById(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        return ResponseEntity.ok(ApiResponse.success("User retrieved successfully", convertToDto(user)));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete user", description = "Deletes a user account by ID")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long id) {
        User user = userService.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with ID " + id + " not found"));
        userService.deleteUser(id);
        return ResponseEntity.ok(ApiResponse.success("User deleted successfully"));
    }

    // Helper method for DTO conversion
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());

        // Convert Set<Role> to single String for DTO
        if (user.getRoles() != null && !user.getRoles().isEmpty()) {
            dto.setRole(user.getRoles().iterator().next().getName());
        }

        dto.setPassword(null); // hide password in responses
        return dto;
    }
}
