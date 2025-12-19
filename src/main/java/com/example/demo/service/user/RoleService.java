package com.example.demo.service.user;

import com.example.demo.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    Role createRole(String name, String description);

    Optional<Role> findById(Long id);

    Optional<Role> findByName(String name);

    List<Role> findAll();

    Role updateRole(Long id, String name, String description);

    void deleteRole(Long id);

    boolean existsByName(String name);
}