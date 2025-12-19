package com.example.demo.service.file;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface IFileService {
    public String storeFile(MultipartFile file) throws IOException;
}
