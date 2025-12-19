package com.example.demo.controller;

import com.example.demo.dto.permission.PermissionDTO;
import com.example.demo.service.permission.PermissionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
// Usually only Admins or specific System Managers should access this
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
public class PermissionController {

    private final PermissionService permissionService;

    // CREATE
    @PostMapping
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO dto) {
        PermissionDTO created = permissionService.createPermission(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    // GET ONE
    @GetMapping("/{id}")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<PermissionDTO>> getActivePermissions() {
        return ResponseEntity.ok(permissionService.getActivePermissions());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<PermissionDTO>> getInactivePermissions() {
        return ResponseEntity.ok(permissionService.getInactivePermissions());
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionDTO dto) {
        return ResponseEntity.ok(permissionService.updatePermission(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<PermissionDTO> activatePermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.activatePermission(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<PermissionDTO> deactivatePermission(@PathVariable Long id) {
        return ResponseEntity.ok(permissionService.deactivatePermission(id));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}