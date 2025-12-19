package com.example.demo.service.compliant;

import com.example.demo.dto.compliant.CreateRequest;
import com.example.demo.dto.compliant.UpdateRequest;
import com.example.demo.mapper.CompliantMapper;
import com.example.demo.model.*;
import com.example.demo.repository.*;
import com.example.demo.service.file.FileService;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class CompliantService implements ICompliantService {

    @Autowired
    private CompliantRepository compliantRepository;
    @Autowired
    private OrganizationalUnitRepository organizationalUnitRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FileService fileService;
    private CompliantMapper compliantMapper = Mappers.getMapper(CompliantMapper.class);

   public Compliant createCompliant(CreateRequest createRequest){
      Compliant compliant = compliantMapper.mapCompliantRequestToCompliant(createRequest);
      // Clear any files that might have been incorrectly mapped by the mapper
      compliant.setFiles(new ArrayList<>());

      OrganizationalUnit organizationalUnit = organizationalUnitRepository.findById(createRequest.getOrganizationalUnitId()).orElseThrow(()->new RuntimeException("organizational unit not found"));
      compliant.setOrganizationalUnit(organizationalUnit);
      Category category = categoryRepository.findById(createRequest.getCategoryId()).orElseThrow(()->new RuntimeException("category not found"));
      compliant.setCategory(category);

      // Handle anonymity and user requirement
      Boolean isAnonymous = createRequest.getIsAnonymous();
      Long userId = createRequest.getUserId();

      // Validation rules:
      // - If isAnonymous is true, userId must be null.
      // - If isAnonymous is false, userId is required.
      if (Boolean.TRUE.equals(isAnonymous) && userId != null) {
          throw new RuntimeException("userId must be null when isAnonymous is true");
      }
      if (Boolean.FALSE.equals(isAnonymous)) {
          if (userId == null) {
              throw new RuntimeException("userId is required when isAnonymous is false");
          }
          User user = userRepository.findById(userId).orElseThrow(()->new RuntimeException("user not found"));
          compliant.setUser(user);
          compliant.setIsAnonymous(false);
      } else {
          // If anonymous or not provided, force anonymous and clear user
          compliant.setIsAnonymous(true);
          compliant.setUser(null);
      }

      // Reference number: only generate when anonymous; otherwise leave null
      if (Boolean.TRUE.equals(compliant.getIsAnonymous())) {
          UUID reference_number = UUID.randomUUID();
          compliant.setReferenceNumber(reference_number.toString());
      } else {
          compliant.setReferenceNumber(null);
      }
      final Compliant createdCompliant = compliantRepository.save(compliant);
      
      // Process files if provided
      List<MultipartFile> files = createRequest.getFiles();
      if (files != null && !files.isEmpty()) {
          files.forEach(file -> {
              if (file != null && !file.isEmpty()) {
                  String filePath;
                  try {
                      filePath = fileService.storeFile(file);
                  } catch (IOException e) {
                      throw new RuntimeException("Failed to upload file", e);
                  }
                  if (filePath == null || filePath.isEmpty()) {
                      throw new RuntimeException("File upload returned empty path");
                  }
                  Files fileModel = new Files();
                  fileModel.setFile_path(filePath);
                  fileModel.setCompliant(createdCompliant);
                  createdCompliant.getFiles().add(fileModel);
              }
          });
          return compliantRepository.save(createdCompliant);
      }
      
      return createdCompliant;
    }

    public List<Compliant> getAllComplaints() {
        return compliantRepository.findAll();
    }

    public Compliant getCompliantById(Long id) {
        return compliantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));
    }

    public Compliant getCompliantByReference(String referenceNumber) {
        return compliantRepository.findByReferenceNumber(referenceNumber)
                .orElseThrow(() -> new RuntimeException("Complaint not found with reference: " + referenceNumber));
    }


    public Compliant updateStatus(Long id, com.example.demo.enums.Status status) {
        Compliant compliant = compliantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));
        compliant.setStatus(status);
        return compliantRepository.save(compliant);
    }

    public Compliant updateCompliant(Long id, UpdateRequest updateRequest) {
        Compliant existingCompliant = compliantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));

        // Update fields if provided
        if (updateRequest.getTitle() != null && !updateRequest.getTitle().isEmpty()) {
            existingCompliant.setTitle(updateRequest.getTitle());
        }
        if (updateRequest.getDescription() != null && !updateRequest.getDescription().isEmpty()) {
            existingCompliant.setDescription(updateRequest.getDescription());
        }
        if (updateRequest.getIsAnonymous() != null) {
            existingCompliant.setIsAnonymous(updateRequest.getIsAnonymous());
        }
        if (updateRequest.getOrganizationalUnitId() != null) {
            OrganizationalUnit organizationalUnit = organizationalUnitRepository.findById(updateRequest.getOrganizationalUnitId())
                    .orElseThrow(() -> new RuntimeException("Organizational unit not found"));
            existingCompliant.setOrganizationalUnit(organizationalUnit);
        }
        if (updateRequest.getCategoryId() != null) {
            Category category = categoryRepository.findById(updateRequest.getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            existingCompliant.setCategory(category);
        }
        if (updateRequest.getUserId() != null) {
            User user = userRepository.findById(updateRequest.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingCompliant.setUser(user);
        }

        // Process new files if provided
        List<MultipartFile> files = updateRequest.getFiles();
        if (files != null && !files.isEmpty()) {
            final Compliant compliantToUpdate = existingCompliant;
            files.forEach(file -> {
                if (file != null && !file.isEmpty()) {
                    String filePath;
                    try {
                        filePath = fileService.storeFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to upload file", e);
                    }
                    if (filePath == null || filePath.isEmpty()) {
                        throw new RuntimeException("File upload returned empty path");
                    }
                    Files fileModel = new Files();
                    fileModel.setFile_path(filePath);
                    fileModel.setCompliant(compliantToUpdate);
                    compliantToUpdate.getFiles().add(fileModel);
                }
            });
        }

        return compliantRepository.save(existingCompliant);
    }

    public void deleteCompliant(Long id) {
        Compliant compliant = compliantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Complaint not found with id: " + id));
        compliantRepository.delete(compliant);
    }
}
