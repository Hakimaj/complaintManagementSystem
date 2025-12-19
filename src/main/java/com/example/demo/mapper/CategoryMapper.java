package com.example.demo.mapper;

import com.example.demo.dto.category.CategoryDTO;
import com.example.demo.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Category.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryDTO toDto(Category entity);
    Category toEntity(CategoryDTO dto);
    void updateEntityFromDto(CategoryDTO dto, @MappingTarget Category entity);
    List<CategoryDTO> toDtoList(List<Category> entities);
}

