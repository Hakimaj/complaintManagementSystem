package com.example.demo.service.role;

import com.example.demo.dto.role.RoleDTO;

import java.util.List;

/**
 * Service interface for Role operations.
 * All methods use DTOs to keep controllers decoupled from persistence entities.
 */
public interface RoleService {
    List<RoleDTO> findAll();
    RoleDTO findById(Long id);
    RoleDTO findByName(String name);
    RoleDTO create(RoleDTO dto);
    RoleDTO update(Long id, RoleDTO dto);
    void delete(Long id);
    boolean existsByName(String name);
    RoleDTO activateRole(Long id);

    RoleDTO deactivateRole(Long id);

    List<RoleDTO> getActiveRoles();

    List<RoleDTO> getInactiveRoles();
}
