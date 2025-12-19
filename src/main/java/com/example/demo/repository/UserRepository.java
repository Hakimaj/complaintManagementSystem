package com.example.demo.repository;

import com.example.demo.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByStudentId(String studentId);

    List<User> findByRoleName(String roleName);

    List<User> findByIsActive(Boolean isActive);
    boolean existsByRole_Id(Long id);

    // Custom queries used by UserServiceImpl
    @Query("SELECT u FROM User u WHERE " +
            "LOWER(CONCAT(u.firstName,' ',u.lastName,' ',u.email)) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<User> findBySearchTerm(@Param("search") String search, Pageable pageable);

    List<User> findByRoleNameAndIsActive(String roleName, Boolean isActive);

    @Query("SELECT u FROM User u WHERE u.role.name = 'ADMIN' AND u.isActive = true")
    List<User> findAllAdmins();

    @Query("SELECT u FROM User u WHERE u.role.name = 'STAFF' AND u.isActive = true")
    List<User> findAllStaff();

    @Query("SELECT u FROM User u WHERE u.role.name = 'USER' AND u.isActive = true")
    List<User> findAllUsers();

    List<User> findByOrganizationalUnitId(Long organizationalUnitId);
}

