package com.example.demo.dto.organizationalUnitType;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrganizationalUnitResponseDTO {
    private Long id;
    private String publicId;
    private String name;
    private String abbreviation;

    // Type Info
    private Long unitTypeId;
    private String unitTypeName; // e.g., "FACULTY"

    // Hierarchy Info
    private Long parentId;
    private String parentName;   // e.g., "Bahir Dar University"

    // Contact
    private String unitEmail;
    private String phoneNumber;

    // Audit
    private Integer status;
    private LocalDateTime createTime;
}