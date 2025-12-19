package com.example.demo.repository;

import com.example.demo.model.Files;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<Files,Long> {
}
