package com.example.demo.service.user;

import com.example.demo.model.OrganizationalUnit;

import java.util.List;
import java.util.Optional;

public interface OrganizationalUnitService {

    OrganizationalUnit createOrganizationalUnit(String name, Long parentId, Long unitTypeId);

    Optional<OrganizationalUnit> findById(Long id);

    Optional<OrganizationalUnit> findByName(String name);

    List<OrganizationalUnit> findAll();

    List<OrganizationalUnit> findByParentId(Long parentId);

    List<OrganizationalUnit> findByUnitTypeId(Long unitTypeId);

    OrganizationalUnit updateOrganizationalUnit(Long id, String name, Long parentId, Long unitTypeId);

    void deleteOrganizationalUnit(Long id);

    boolean existsByName(String name);
}