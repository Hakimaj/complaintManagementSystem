package com.example.demo.service.user;

import com.example.demo.dto.user.OrganizationalUnitTypeDto;
import com.example.demo.model.OrganizationalUnitType;

import java.util.List;
import java.util.Optional;

public interface OrganizationalUnitTypeService {

    // Entity methods (for internal use)
    OrganizationalUnitType createOrganizationalUnitType(String name);
    Optional<OrganizationalUnitType> findById(Long id);
    Optional<OrganizationalUnitType> findByName(String name);
    List<OrganizationalUnitType> findAll();
    OrganizationalUnitType updateOrganizationalUnitType(Long id, String name);
    void deleteOrganizationalUnitType(Long id);
    boolean existsByName(String name);

    // DTO methods (for API use)
    OrganizationalUnitTypeDto createOrganizationalUnitTypeDto(OrganizationalUnitTypeDto organizationalUnitTypeDto);
    Optional<OrganizationalUnitTypeDto> findByIdDto(Long id);
    List<OrganizationalUnitTypeDto> findAllDto();
    OrganizationalUnitTypeDto updateOrganizationalUnitTypeDto(Long id, OrganizationalUnitTypeDto organizationalUnitTypeDto);
}