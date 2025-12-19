package com.example.demo.dto.permission;

import lombok.Data;

@Data
public class PermissionDTO {
    private Long id;
    private String name;
    private String description;
    private String category;
    private Boolean isActive;
}