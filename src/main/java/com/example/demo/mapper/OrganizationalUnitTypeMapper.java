package com.example.demo.mapper;

import com.example.demo.dto.user.OrganizationalUnitTypeDto;
import com.example.demo.model.OrganizationalUnitType;
import org.mapstruct.*;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface OrganizationalUnitTypeMapper {

    OrganizationalUnitTypeDto toDto(OrganizationalUnitType organizationalUnitType);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    OrganizationalUnitType toEntity(OrganizationalUnitTypeDto organizationalUnitTypeDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    void updateEntityFromDto(OrganizationalUnitTypeDto organizationalUnitTypeDto, @MappingTarget OrganizationalUnitType organizationalUnitType);
}