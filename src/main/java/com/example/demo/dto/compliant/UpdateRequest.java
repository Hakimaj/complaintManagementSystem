package com.example.demo.dto.compliant;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateRequest {
    private String title;
    private String description;
    private Boolean isAnonymous;
    private Long organizationalUnitId;
    private Long categoryId;
    private Long userId;
    private List<MultipartFile> files;
}

