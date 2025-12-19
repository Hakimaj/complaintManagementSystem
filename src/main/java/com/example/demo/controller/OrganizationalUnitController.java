package com.example.demo.controller;

import com.example.demo.dto.organizationalUnit.OrganizationalUnitRequestDTO;
import com.example.demo.dto.organizationalUnitType.OrganizationalUnitResponseDTO;
import com.example.demo.service.organizationalUnit.OrganizationalUnitService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/units")
@RequiredArgsConstructor
public class OrganizationalUnitController {

    private final OrganizationalUnitService unitService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizationalUnitResponseDTO> createUnit(@Valid @RequestBody OrganizationalUnitRequestDTO requestDTO) {
        return new ResponseEntity<>(unitService.createUnit(requestDTO), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<OrganizationalUnitResponseDTO>> getAllUnits() {
        return ResponseEntity.ok(unitService.getAllUnits());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationalUnitResponseDTO> getUnitById(@PathVariable Long id) {
        return ResponseEntity.ok(unitService.getUnitById(id));
    }

    // e.g., GET /api/v1/units/type/1 (Returns all Faculties)
    @GetMapping("/type/{typeId}")
    public ResponseEntity<List<OrganizationalUnitResponseDTO>> getUnitsByType(@PathVariable Long typeId) {
        return ResponseEntity.ok(unitService.getUnitsByType(typeId));
    }

    // e.g., GET /api/v1/units/parent/5 (Returns all Depts in Faculty #5)
    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<OrganizationalUnitResponseDTO>> getUnitsByParent(@PathVariable Long parentId) {
        return ResponseEntity.ok(unitService.getUnitsByParent(parentId));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrganizationalUnitResponseDTO> updateUnit(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationalUnitRequestDTO requestDTO) {
        return ResponseEntity.ok(unitService.updateUnit(id, requestDTO));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUnit(@PathVariable Long id) {
        unitService.deleteUnit(id);
        return ResponseEntity.noContent().build();
    }
}