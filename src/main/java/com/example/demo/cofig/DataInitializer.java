package com.example.demo.cofig;

import com.example.demo.dto.user.CreateUserDto;
import com.example.demo.model.Role;
import com.example.demo.service.user.RoleService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeDefaultAdmin();
    }

    private void initializeRoles() {
        try {
            // Create ADMIN role
            if (!roleService.existsByName("ADMIN")) {
                roleService.createRole("ADMIN", "System Administrator with full access");
                log.info("Created ADMIN role");
            }

            // Create STAFF role (Complaint Officers)
            if (!roleService.existsByName("STAFF")) {
                roleService.createRole("STAFF", "Staff members who handle complaints");
                log.info("Created STAFF role");
            }

            // Create USER role (Students and other complainants)
            if (!roleService.existsByName("USER")) {
                roleService.createRole("USER", "Regular users who can submit complaints");
                log.info("Created USER role");
            }

        } catch (Exception e) {
            log.error("Error initializing roles: {}", e.getMessage());
        }
    }

    private void initializeDefaultAdmin() {
        try {
            // Create default admin user if not exists
            String adminEmail = "admin@university.edu";
            if (!userService.existsByEmail(adminEmail)) {
                Role adminRole = roleService.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                CreateUserDto adminDto = new CreateUserDto();
                adminDto.setFirstName("System");
                adminDto.setLastName("Administrator");
                adminDto.setEmail(adminEmail);
                adminDto.setPassword("admin123"); // Change this in production
                adminDto.setRoleId(adminRole.getId());

                userService.createUser(adminDto);
                log.info("Created default admin user with email: {}", adminEmail);
                log.info("Default admin password: admin123 (Please change this in production!)");
            }

        } catch (Exception e) {
            log.error("Error initializing default admin: {}", e.getMessage());
        }
    }
}