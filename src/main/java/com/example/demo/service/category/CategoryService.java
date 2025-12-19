package com.example.demo.service.category;

import com.example.demo.dto.category.CategoryDTO;

import java.util.List;

/**
 * Service interface for Category operations.
 */
public interface CategoryService {
    List<CategoryDTO> findAll();
    CategoryDTO findById(Long id);
    CategoryDTO findByName(String name);
    CategoryDTO create(CategoryDTO dto);
    CategoryDTO update(Long id, CategoryDTO dto);
    void delete(Long id);
    boolean existsByName(String name);
    CategoryDTO activateCategory(Long id);

    CategoryDTO deactivateCategory(Long id);

    List<CategoryDTO> getActiveCategories();

    List<CategoryDTO> getInactiveCategories();
}

