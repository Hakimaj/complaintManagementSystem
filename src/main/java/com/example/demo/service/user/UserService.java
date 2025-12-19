package com.example.demo.service.user;

import com.example.demo.dto.user.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface UserService {

    // Registration (public - assigns USER role automatically)
    UserDto registerUser(RegisterDto registerDto);

    // Basic CRUD operations (admin only - allows role assignment)
    UserDto createUser(CreateUserDto createUserDto);

    UserDto updateUser(Long id, UpdateUserDto updateUserDto);

    void deleteUser(Long id);

    Optional<UserDto> getUserById(Long id);

    Optional<UserDto> getUserByEmail(String email);

    List<UserDto> getAllUsers();

    Page<UserDto> getAllUsers(Pageable pageable);

    Page<UserDto> searchUsers(String search, Pageable pageable);

    // Role-based queries
    List<UserDto> getUsersByRole(String roleName);

    List<UserDto> getActiveUsersByRole(String roleName);

    List<UserDto> getAllAdmins();

    List<UserDto> getAllStaff();

    List<UserDto> getAllRegularUsers();

    // Organizational unit queries
    List<UserDto> getUsersByOrganizationalUnit(Long unitId);

    // User status management
    UserDto activateUser(Long id);

    UserDto deactivateUser(Long id);

    List<UserDto> getActiveUsers();

    List<UserDto> getInactiveUsers();

    // Password management
    void changePassword(Long userId, ChangePasswordDto changePasswordDto);

    void resetPassword(Long userId, String newPassword);

    // Validation methods
    boolean existsByEmail(String email);

    boolean existsByStudentId(String studentId);


}