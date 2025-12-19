package com.example.demo.service.category;

import com.example.demo.repository.CompliantRepository;
import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.mapper.CategoryMapper;
import com.example.demo.model.Category;
import com.example.demo.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CategoryServiceImpl uses CategoryMapper for conversions.
 * Prevents deletion when complaints reference the category.
 */
@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CompliantRepository complaintRepository;
    private final CategoryMapper categoryMapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll() {
        return categoryMapper.toDtoList(categoryRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + id));
        return categoryMapper.toDto(c);
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO findByName(String name) {
        Category c = categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFound("Category not found with name: " + name));
        return categoryMapper.toDto(c);
    }

    @Override
    @Transactional
    public CategoryDTO create(CategoryDTO dto) {
        String clean = dto.getName().trim();
        if (categoryRepository.existsByName(clean)) {
            throw new DuplicateResourceException("Category with name '" + clean + "' already exists");
        }
        dto.setName(clean);
        Category saved = categoryRepository.save(categoryMapper.toEntity(dto));
        return categoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CategoryDTO update(Long id, CategoryDTO dto) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + id));

        String newName = dto.getName().trim();
        if (!existing.getName().equalsIgnoreCase(newName) && categoryRepository.existsByName(newName)) {
            throw new DuplicateResourceException("Category with name '" + newName + "' already exists");
        }

        dto.setName(newName);
        categoryMapper.updateEntityFromDto(dto, existing);
        Category saved = categoryRepository.save(existing);
        return categoryMapper.toDto(saved);
    }

    @Override
    @Transactional
    public CategoryDTO activateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + id)); // Use ResourceNotFound()
        category.setIsActive(true);
        // Use categoryMapper.toDto and ensure the method is @Transactional
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO deactivateCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Category not found with id: " + id)); // Use ResourceNotFound()
        category.setIsActive(false);
        // Use categoryMapper.toDto and ensure the method is @Transactional
        return categoryMapper.toDto(categoryRepository.save(category));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getActiveCategories() { // Renamed to reflect Category domain
        // Use categoryMapper.toDtoList for consistency
        return categoryMapper.toDtoList(categoryRepository.findByIsActive(true));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getInactiveCategories() { // Renamed to reflect Category domain and return type
        // The original code incorrectly referenced 'userRepository' and 'UserDto'.
        // Assuming the intention was to return inactive *Categories* using *CategoryRepository*.
        // Use categoryMapper.toDtoList for consistency
        return categoryMapper.toDtoList(categoryRepository.findByIsActive(false));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFound("Category not found with id: " + id);
        }
        if (complaintRepository.existsByCategoryId(id)) {
            throw new IllegalStateException("Cannot delete category with id " + id + " — complaints reference it");
        }
        categoryRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return categoryRepository.existsByName(name);
    }
}
