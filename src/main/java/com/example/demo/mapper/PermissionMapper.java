package com.example.demo.mapper;

import com.example.demo.dto.permission.PermissionDTO;
import com.example.demo.model.Permission;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PermissionMapper {
    PermissionDTO toDto(Permission permission);
    Permission toEntity(PermissionDTO dto);
    List<PermissionDTO> toDtoList(List<Permission> permissions);
}