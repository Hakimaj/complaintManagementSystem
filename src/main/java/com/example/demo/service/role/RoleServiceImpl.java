package com.example.demo.service.role;

import com.example.demo.dto.role.RoleDTO;
import com.example.demo.exception.DuplicateResourceException;
import com.example.demo.exception.ResourceNotFound;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.model.Role;
import com.example.demo.repository.RoleRepository;
import com.example.demo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Role service implementation using MapStruct mapper.
 * - Keeps mapping logic centralized in RoleMapper
 * - Keeps transactional boundaries on service methods
 */
@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;


    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> findAll() {
        return roleMapper.toDtoList(roleRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findById(Long id) {
        Role r = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Role not found with id: " + id));
        return roleMapper.toDto(r);
    }

    @Override
    @Transactional(readOnly = true)
    public RoleDTO findByName(String name) {
        Role r = roleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFound("Role not found with name: " + name));
        return roleMapper.toDto(r);
    }

    @Override
    @Transactional
    public RoleDTO create(RoleDTO dto) {
        String clean = dto.getName().trim();
        if (roleRepository.existsByName(clean)) {
            throw new DuplicateResourceException("Role with name '" + clean + "' already exists");
        }

        dto.setName(clean); // sanitize before mapping
        Role saved = roleRepository.save(roleMapper.toEntity(dto));
        return roleMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoleDTO update(Long id, RoleDTO dto) {
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Role not found with id: " + id));

        String newName = dto.getName().trim();
        if (!existing.getName().equalsIgnoreCase(newName) && roleRepository.existsByName(newName)) {
            throw new DuplicateResourceException("Role with name '" + newName + "' already exists");
        }

        // Use mapper to update the entity in-place (keeps other fields intact)
        dto.setName(newName);
        roleMapper.updateEntityFromDto(dto, existing);
        Role saved = roleRepository.save(existing);
        return roleMapper.toDto(saved);
    }

    @Override
    @Transactional
    public RoleDTO activateRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Role not found with id: " + id));
        role.setIsActive(true);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional
    public RoleDTO deactivateRole(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFound("Role not found with id: " + id));
        // You might want to add a check here to prevent deactivating a role if it's currently assigned to active users.
        role.setIsActive(false);
        return roleMapper.toDto(roleRepository.save(role));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getActiveRoles() {
        // Assuming RoleRepository has findByIsActive(boolean)
        return roleMapper.toDtoList(roleRepository.findByIsActive(true));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RoleDTO> getInactiveRoles() {
        return roleMapper.toDtoList(roleRepository.findByIsActive(false));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new ResourceNotFound("Role not found with id: " + id);
        }
        // Prevent deleting roles that are in use by users
        if (userRepository.existsByRole_Id(id)) {
            throw new IllegalStateException("Cannot delete role with id " + id + " — users are assigned to it");
        }
        roleRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return roleRepository.existsByName(name);
    }
}

