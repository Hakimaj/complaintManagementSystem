package com.example.demo.controller;

import com.example.demo.dto.role.RoleDTO;
import com.example.demo.service.role.RoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

/**
 * Role REST controller. Returns DTOs and uses HTTP status codes properly.
 */
@RestController
@PreAuthorize("hasAuthority('ROLE_MANAGE')")
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    public ResponseEntity<RoleDTO> create(@Valid @RequestBody RoleDTO dto) {
        RoleDTO created = roleService.create(dto);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<RoleDTO>> getAll() {
        return ResponseEntity.ok(roleService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RoleDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.findById(id));
    }

    @GetMapping("/active")
    public ResponseEntity<List<RoleDTO>> getActiveRoles() {
        return ResponseEntity.ok(roleService.getActiveRoles());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<RoleDTO>> getInactiveRoles() {
        return ResponseEntity.ok(roleService.getInactiveRoles());
    }

    @PutMapping("/{id}")
    public ResponseEntity<RoleDTO> update(@PathVariable Long id, @Valid @RequestBody RoleDTO dto) {
        return ResponseEntity.ok(roleService.update(id, dto));
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<RoleDTO> activateRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.activateRole(id));
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<RoleDTO> deactivateRole(@PathVariable Long id) {
        return ResponseEntity.ok(roleService.deactivateRole(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

