package com.example.demo.controller;

import com.example.demo.dto.user.ChangePasswordDto;
import com.example.demo.dto.user.UpdateUserDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserProfileController {

    private final UserService userService;

    // Get current user's profile
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUserProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        Optional<UserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update current user's profile
    @PutMapping("/me")
    public ResponseEntity<UserDto> updateCurrentUserProfile(@Valid @RequestBody UpdateUserDto updateUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        try {
            Optional<UserDto> currentUser = userService.getUserByEmail(email);
            if (currentUser.isPresent()) {
                UserDto updatedUser = userService.updateUser(currentUser.get().getId(), updateUserDto);
                return ResponseEntity.ok(updatedUser);
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Change current user's password
    @PatchMapping("/change-password")
    public ResponseEntity<String> changeCurrentUserPassword(@Valid @RequestBody ChangePasswordDto changePasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        
        try {
            Optional<UserDto> currentUser = userService.getUserByEmail(email);
            if (currentUser.isPresent()) {
                userService.changePassword(currentUser.get().getId(), changePasswordDto);
                return ResponseEntity.ok("Password changed successfully");
            }
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // Get user by email (for staff and admin to view other users)
    @GetMapping("/user/email/{email}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        Optional<UserDto> user = userService.getUserByEmail(email);
        return user.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get users by organizational unit (for staff and admin)
    @GetMapping("/organizational-unit/{unitId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<UserDto>> getUsersByOrganizationalUnit(@PathVariable Long unitId) {
        List<UserDto> users = userService.getUsersByOrganizationalUnit(unitId);
        return ResponseEntity.ok(users);
    }

    // Get staff members (for staff and admin)
    @GetMapping("/staff")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<List<UserDto>> getAllStaff() {
        List<UserDto> staff = userService.getAllStaff();
        return ResponseEntity.ok(staff);
    }

    // Check if email exists (public for registration validation)
    @GetMapping("/exists/email/{email}")
    public ResponseEntity<Boolean> existsByEmail(@PathVariable String email) {
        boolean exists = userService.existsByEmail(email);
        return ResponseEntity.ok(exists);
    }

    // Check if student ID exists (public for registration validation)
    @GetMapping("/exists/student-id/{studentId}")
    public ResponseEntity<Boolean> existsByStudentId(@PathVariable String studentId) {
        boolean exists = userService.existsByStudentId(studentId);
        return ResponseEntity.ok(exists);
    }
}