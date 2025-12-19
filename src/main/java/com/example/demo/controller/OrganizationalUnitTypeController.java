package com.example.demo.controller;

import com.example.demo.model.OrganizationalUnitType;
import com.example.demo.repository.OrganizationalUnitTypeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/unit-types")
@RequiredArgsConstructor
public class OrganizationalUnitTypeController {

    private final OrganizationalUnitTypeRepository repository;



    // CREATE
    @PostMapping
    public ResponseEntity<OrganizationalUnitType> createType(@Valid @RequestBody OrganizationalUnitType unitType) {
        if (repository.existsByName(unitType.getName())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Type name already exists");
        }
        return new ResponseEntity<>(repository.save(unitType), HttpStatus.CREATED);
    }

    // GET ALL
    @GetMapping
    public List<OrganizationalUnitType> getAllTypes() {
        return repository.findAll();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<OrganizationalUnitType> updateType(@PathVariable Long id, @Valid @RequestBody OrganizationalUnitType updates) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setName(updates.getName());
                    return new ResponseEntity<>(repository.save(existing), HttpStatus.OK);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Type not found"));
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteType(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Type not found");
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}