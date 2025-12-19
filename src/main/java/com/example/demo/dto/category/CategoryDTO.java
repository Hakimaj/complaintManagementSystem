package com.example.demo.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Lightweight Category DTO — exposes only id + name.
 */
@Data
public class CategoryDTO {
    private Long id;

    @NotBlank(message = "Category name is required")
    private String name;
    private Boolean isActive;
}
