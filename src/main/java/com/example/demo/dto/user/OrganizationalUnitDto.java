package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationalUnitDto {
    private Long id;
    private String name;
    private String parentName;
    private String unitTypeName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}