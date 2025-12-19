package com.example.demo.controller;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.service.category.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/org/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // Creation is for Admins only
    @PostMapping
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    public ResponseEntity<CategoryDTO> create(@Valid @RequestBody CategoryDTO dto) {
        CategoryDTO created = categoryService.create(dto);
        return ResponseEntity.created(URI.create("/api/org/categories/" + created.getId())).body(created);
    }

    // Viewing should be allowed for Users too (so they can select a category in the form)
    // If our Permission Enum has "ORG_VIEW", use that. Or "isAuthenticated()"
    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CategoryDTO>> getAll() {
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CategoryDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(categoryService.findById(id));
    }

    // Update is Admin only
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    public ResponseEntity<CategoryDTO> update(@PathVariable Long id, @Valid @RequestBody CategoryDTO dto) {
        return ResponseEntity.ok(categoryService.update(id, dto));
    }


    // Activate/Deactivate (Using Long)
    @PatchMapping("/{id}/activate")
    public ResponseEntity<CategoryDTO> activateCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.activateCategory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<CategoryDTO> deactivateCategory(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(categoryService.deactivateCategory(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    @GetMapping("/active")
    public ResponseEntity<List<CategoryDTO>> getActiveCategories() {
        return ResponseEntity.ok(categoryService.getActiveCategories());
    }

    @GetMapping("/inactive")
    public ResponseEntity<List<CategoryDTO>> getInactiveUsers() {
        return ResponseEntity.ok(categoryService.getInactiveCategories());
    }

    // Delete is Admin only
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('CATEGORY_MANAGE')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}