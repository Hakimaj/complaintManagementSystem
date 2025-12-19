package com.example.demo.repository;

import com.example.demo.model.Role;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {

    boolean existsByName(String name);

    Optional<Role> findByName(@NotBlank(message = "Role name cannot be empty") String name);

    List<Role> findByIsActive(boolean isActive);
}