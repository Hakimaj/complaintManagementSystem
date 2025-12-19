package com.example.demo.mapper;

import com.example.demo.dto.role.RoleDTO;
import com.example.demo.model.Role;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

import java.util.List;

/**
 * MapStruct mapper for Role <-> RoleDTO.
 * componentModel = "spring" allows Spring to inject the mapper.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface RoleMapper {
    RoleDTO toDto(Role entity);
    Role toEntity(RoleDTO dto);

    // update existing entity from dto (used in update operations)
    void updateEntityFromDto(RoleDTO dto, @MappingTarget Role entity);

    // convenience for lists
    List<RoleDTO> toDtoList(List<Role> entities);
}

