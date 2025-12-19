package com.example.demo.controller;

import com.example.demo.dto.user.ApiResponse;
import com.example.demo.dto.user.CreateUserDto;
import com.example.demo.dto.user.UpdateUserDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    // Create a new user (Admin only - allows role assignment)
    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        UserDto createdUser = userService.createUser(createUserDto);
        return new ResponseEntity<>(ApiResponse.success("User created successfully", createdUser), HttpStatus.CREATED);
    }

    // Get all users with pagination
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserDto>>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String search) {

        Sort sort = sortDir.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<UserDto> users;
        if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }

        return ResponseEntity.ok(ApiResponse.success(users));
    }

    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        Optional<UserDto> user = userService.getUserById(id);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id,
                                              @Valid @RequestBody UpdateUserDto updateUserDto) {
        try {
            UserDto updatedUser = userService.updateUser(id, updateUserDto);
            return ResponseEntity.ok(updatedUser);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get all admins
    @GetMapping("/admins")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        List<UserDto> admins = userService.getAllAdmins();
        return ResponseEntity.ok(admins);
    }

    // Get all staff
    @GetMapping("/staff")
    public ResponseEntity<List<UserDto>> getAllStaff() {
        List<UserDto> staff = userService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    // Get all regular users
    @GetMapping("/regular-users")
    public ResponseEntity<List<UserDto>> getAllRegularUsers() {
        List<UserDto> users = userService.getAllRegularUsers();
        return ResponseEntity.ok(users);
    }

    // Get users by organizational unit
    @GetMapping("/organizational-unit/{unitId}")
    public ResponseEntity<List<UserDto>> getUsersByOrganizationalUnit(@PathVariable Long unitId) {
        List<UserDto> users = userService.getUsersByOrganizationalUnit(unitId);
        return ResponseEntity.ok(users);
    }

    // Activate user
    @PatchMapping("/{id}/activate")
    public ResponseEntity<UserDto> activateUser(@PathVariable Long id) {
        try {
            UserDto user = userService.activateUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Deactivate user
    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<UserDto> deactivateUser(@PathVariable Long id) {
        try {
            UserDto user = userService.deactivateUser(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Get active users
    @GetMapping("/active")
    public ResponseEntity<List<UserDto>> getActiveUsers() {
        List<UserDto> users = userService.getActiveUsers();
        return ResponseEntity.ok(users);
    }

    // Get inactive users
    @GetMapping("/inactive")
    public ResponseEntity<List<UserDto>> getInactiveUsers() {
        List<UserDto> users = userService.getInactiveUsers();
        return ResponseEntity.ok(users);
    }

    // Reset password (Admin only)
    @PatchMapping("/{id}/reset-password")
    public ResponseEntity<String> resetPassword(@PathVariable Long id,
                                                @RequestBody String newPassword) {
        try {
            userService.resetPassword(id, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get users by role
    @GetMapping("/role/{roleName}")
    public ResponseEntity<List<UserDto>> getUsersByRole(@PathVariable String roleName) {
        List<UserDto> users = userService.getUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }

    // Get active users by role
    @GetMapping("/role/{roleName}/active")
    public ResponseEntity<List<UserDto>> getActiveUsersByRole(@PathVariable String roleName) {
        List<UserDto> users = userService.getActiveUsersByRole(roleName);
        return ResponseEntity.ok(users);
    }
}