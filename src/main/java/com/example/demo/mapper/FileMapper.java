package com.example.demo.mapper;

import com.example.demo.dto.file.CreateFile;
import com.example.demo.model.Files;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FileMapper {
    Files mapCreateFileToFile(CreateFile createFile);
}
