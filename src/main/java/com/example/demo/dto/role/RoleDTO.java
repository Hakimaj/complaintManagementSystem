package com.example.demo.dto.role;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * Simple DTO for Role.
 * Only exposes the fields needed by API clients (no user lists, etc.).
 */
@Data
public class RoleDTO {
    private Long id;

    @NotBlank(message = "Role name is required")
    private String name; // e.g. ROLE_ADMIN, ROLE_STUDENT, ROLE_COMPLAINT_OFFICER
    private Boolean isActive;

}
