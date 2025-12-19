package com.example.demo.mapper;

import com.example.demo.dto.organizationalUnit.OrganizationalUnitRequestDTO;
import com.example.demo.dto.organizationalUnitType.OrganizationalUnitResponseDTO;
import com.example.demo.model.OrganizationalUnit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationalUnitMapper {

    // Entity -> Response DTO
    @Mapping(source = "unitType.id", target = "unitTypeId")
    @Mapping(source = "unitType.name", target = "unitTypeName")
    @Mapping(source = "parent.id", target = "parentId")
    @Mapping(source = "parent.name", target = "parentName")
    OrganizationalUnitResponseDTO toResponseDTO(OrganizationalUnit unit);

    // Request DTO -> Entity (Creation)
    // We ignore complex associations here and set them in the Service layer
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitType", ignore = true)
    @Mapping(target = "parent", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrganizationalUnit toEntity(OrganizationalUnitRequestDTO dto);

    // Request DTO -> Entity (Update)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "unitType", ignore = true) // Handled in service if changed
    @Mapping(target = "parent", ignore = true)   // Handled in service if changed
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(OrganizationalUnitRequestDTO dto, @MappingTarget OrganizationalUnit entity);
}