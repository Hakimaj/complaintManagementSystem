package com.example.demo.service.permission;

import com.example.demo.dto.permission.PermissionDTO;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.mapper.PermissionMapper;
import com.example.demo.model.Permission;
import com.example.demo.repository.PermissionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final PermissionMapper permissionMapper;

    @Transactional(readOnly = true)
    public List<PermissionDTO> getAllPermissions() {
        return permissionMapper.toDtoList(permissionRepository.findAll());
    }

    @Transactional(readOnly = true)
    public PermissionDTO getPermissionById(Long id) {
        Permission p = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with ID: " + id));
        return permissionMapper.toDto(p);
    }

    @Transactional
    public PermissionDTO createPermission(PermissionDTO dto) {
        // Enforce UPPERCASE naming convention for consistency
        String name = dto.getName().trim().toUpperCase();

        if (permissionRepository.existsByName(name)) {
            throw new IllegalArgumentException("Permission '" + name + "' already exists");
        }

        Permission perm = permissionMapper.toEntity(dto);
        perm.setName(name);

        return permissionMapper.toDto(permissionRepository.save(perm));
    }

    @Transactional
    public PermissionDTO updatePermission(Long id, PermissionDTO dto) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with ID: " + id));

        // NOTE: Changing the Name is risky if code relies on it,
        // but we allow it here for the admin panel.
        if (dto.getName() != null && !dto.getName().isBlank()) {
            String newName = dto.getName().trim().toUpperCase();
            if (!existing.getName().equals(newName) && permissionRepository.existsByName(newName)) {
                throw new IllegalArgumentException("Permission Name '" + newName + "' is already taken.");
            }
            existing.setName(newName);
        }

        if (dto.getDescription() != null) existing.setDescription(dto.getDescription());
        if (dto.getCategory() != null) existing.setCategory(dto.getCategory());

        return permissionMapper.toDto(permissionRepository.save(existing));
    }

    @Transactional
    public void deletePermission(Long id) {
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Permission not found with ID: " + id));

        // SAFETY CHECK: Don't delete if it's assigned to roles
        if (!existing.getRoles().isEmpty()) {
            throw new IllegalStateException("Cannot delete Permission. It is assigned to " + existing.getRoles().size() + " roles. Remove it from roles first.");
        }

        permissionRepository.delete(existing);
    }

    // Helper for DataSeeder
    @Transactional
    public Permission createPermissionIfNotFound(String name, String description, String category) {
        return permissionRepository.findByName(name)
                .orElseGet(() -> permissionRepository.save(Permission.builder()
                        .name(name)
                        .description(description)
                        .category(category)
                        .build()));
    }


    @Transactional
    public PermissionDTO activatePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Permission not found with id: " + id));
        permission.setIsActive(true);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }


    @Transactional
    public PermissionDTO deactivatePermission(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Permission not found with id: " + id));
        permission.setIsActive(false);
        return permissionMapper.toDto(permissionRepository.save(permission));
    }


    @Transactional(readOnly = true)
    public List<PermissionDTO> getActivePermissions() {
        // Assuming PermissionRepository has findByIsActive(boolean)
        return permissionMapper.toDtoList(permissionRepository.findByIsActive(true));
    }


    @Transactional(readOnly = true)
    public List<PermissionDTO> getInactivePermissions() {
        return permissionMapper.toDtoList(permissionRepository.findByIsActive(false));
    }
}