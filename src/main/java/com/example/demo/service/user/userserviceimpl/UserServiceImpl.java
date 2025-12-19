package com.example.demo.service.user.userserviceimpl;

import com.example.demo.dto.user.*;
import com.example.demo.mapper.UserMapper;
import com.example.demo.model.OrganizationalUnit;
import com.example.demo.model.Role;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.user.OrganizationalUnitService;
import com.example.demo.service.user.RoleService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final OrganizationalUnitService organizationalUnitService;
    private final UserMapper userMapper;

    @Override
    public UserDto registerUser(RegisterDto registerDto) {
        // Validate email uniqueness
        if (existsByEmail(registerDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + registerDto.getEmail());
        }

        // Validate student ID uniqueness if provided
        if (registerDto.getStudentId() != null && existsByStudentId(registerDto.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + registerDto.getStudentId());
        }

        // Get default USER role
        Role userRole = roleService.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Default USER role not found. Please contact administrator."));

        // Create user with default USER role using MapStruct
        User user = userMapper.toEntity(registerDto);
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        user.setRole(userRole); // Always assign USER role for public registration
        // organizationalUnit remains null for regular users

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto createUser(CreateUserDto createUserDto) {
        // Validate email uniqueness
        if (existsByEmail(createUserDto.getEmail())) {
            throw new RuntimeException("Email already exists: " + createUserDto.getEmail());
        }

        // Validate student ID uniqueness if provided
        if (createUserDto.getStudentId() != null && existsByStudentId(createUserDto.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + createUserDto.getStudentId());
        }

        User user = userMapper.toEntity(createUserDto);
        user.setPassword(passwordEncoder.encode(createUserDto.getPassword()));

        // Set role
        Role role = roleService.findById(createUserDto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + createUserDto.getRoleId()));
        user.setRole(role);

        // Set organizational unit if provided
        if (createUserDto.getOrganizationalUnitId() != null) {
            OrganizationalUnit unit = organizationalUnitService.findById(createUserDto.getOrganizationalUnitId())
                    .orElseThrow(() -> new RuntimeException("Organizational unit not found with id: " + createUserDto.getOrganizationalUnitId()));
            user.setOrganizationalUnit(unit);
        }

        User savedUser = userRepository.save(user);
        return userMapper.toDto(savedUser);
    }

    @Override
    public UserDto updateUser(Long id, UpdateUserDto updateUserDto) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Validate email uniqueness if email is being changed
        if (updateUserDto.getEmail() != null && !updateUserDto.getEmail().equals(user.getEmail())) {
            if (existsByEmail(updateUserDto.getEmail())) {
                throw new RuntimeException("Email already exists: " + updateUserDto.getEmail());
            }
        }

        // Validate student ID uniqueness if student ID is being changed
        if (updateUserDto.getStudentId() != null && !updateUserDto.getStudentId().equals(user.getStudentId())) {
            if (existsByStudentId(updateUserDto.getStudentId())) {
                throw new RuntimeException("Student ID already exists: " + updateUserDto.getStudentId());
            }
        }

        // Use mapper to update basic fields
        userMapper.updateEntityFromDto(updateUserDto, user);

        // Handle role update if provided
        if (updateUserDto.getRoleId() != null) {
            Role role = roleService.findById(updateUserDto.getRoleId())
                    .orElseThrow(() -> new RuntimeException("Role not found with id: " + updateUserDto.getRoleId()));
            user.setRole(role);
        }

        // Handle organizational unit update if provided
        if (updateUserDto.getOrganizationalUnitId() != null) {
            OrganizationalUnit unit = organizationalUnitService.findById(updateUserDto.getOrganizationalUnitId())
                    .orElseThrow(() -> new RuntimeException("Organizational unit not found with id: " + updateUserDto.getOrganizationalUnitId()));
            user.setOrganizationalUnit(unit);
        }

        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserById(Long id) {
        return userRepository.findById(id).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<UserDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllUsers() {
        return userRepository.findAll().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> searchUsers(String search, Pageable pageable) {
        return userRepository.findBySearchTerm(search, pageable).map(userMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(String roleName) {
        return userRepository.findByRoleName(roleName).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getActiveUsersByRole(String roleName) {
        return userRepository.findByRoleNameAndIsActive(roleName, true).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllAdmins() {
        return userRepository.findAllAdmins().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllStaff() {
        return userRepository.findAllStaff().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getAllRegularUsers() {
        return userRepository.findAllUsers().stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByOrganizationalUnit(Long unitId) {
        return userRepository.findByOrganizationalUnitId(unitId).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto activateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(true);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    public UserDto deactivateUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        user.setIsActive(false);
        User updatedUser = userRepository.save(user);
        return userMapper.toDto(updatedUser);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getActiveUsers() {
        return userRepository.findByIsActive(true).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getInactiveUsers() {
        return userRepository.findByIsActive(false).stream()
                .map(userMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public void changePassword(Long userId, ChangePasswordDto changePasswordDto) {
        if (!changePasswordDto.getNewPassword().equals(changePasswordDto.getConfirmPassword())) {
            throw new RuntimeException("New password and confirm password do not match");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        if (!passwordEncoder.matches(changePasswordDto.getCurrentPassword(), user.getPassword())) {
            throw new RuntimeException("Current password is incorrect");
        }

        user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByStudentId(String studentId) {
        return studentId != null && userRepository.existsByStudentId(studentId);
    }


}