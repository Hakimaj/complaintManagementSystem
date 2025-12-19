package com.example.demo.service.organizationalUnit;

import com.example.demo.dto.organizationalUnit.OrganizationalUnitRequestDTO;
import com.example.demo.dto.organizationalUnitType.OrganizationalUnitResponseDTO;
import com.example.demo.mapper.OrganizationalUnitMapper;
import com.example.demo.model.OrganizationalUnit;
import com.example.demo.model.OrganizationalUnitType;
import com.example.demo.repository.OrganizationalUnitRepository;
import com.example.demo.repository.OrganizationalUnitTypeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrganizationalUnitService {

    private final OrganizationalUnitRepository unitRepository;
    private final OrganizationalUnitTypeRepository typeRepository;
    private final OrganizationalUnitMapper unitMapper;

    @Transactional
    public OrganizationalUnitResponseDTO createUnit(OrganizationalUnitRequestDTO requestDTO) {
        // 1. Validate Unique Name
        if (unitRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Unit with name '" + requestDTO.getName() + "' already exists.");
        }

        // 2. Fetch Unit Type
        OrganizationalUnitType type = typeRepository.findById(requestDTO.getUnitTypeId())
                .orElseThrow(() -> new EntityNotFoundException("Unit Type not found ID: " + requestDTO.getUnitTypeId()));

        // 3. Convert DTO to Entity
        OrganizationalUnit unit = unitMapper.toEntity(requestDTO);
        unit.setUnitType(type);

        // 4. Handle Parent (if provided)
        if (requestDTO.getParentId() != null) {
            OrganizationalUnit parent = unitRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Unit not found ID: " + requestDTO.getParentId()));

            // Basic Cycle Detection (A unit cannot be its own parent)
            if (parent.getId().equals(unit.getId())) {
                throw new IllegalArgumentException("A unit cannot be its own parent.");
            }
            unit.setParent(parent);
        }

        // 5. Save
        OrganizationalUnit savedUnit = unitRepository.save(unit);
        return unitMapper.toResponseDTO(savedUnit);
    }

    @Transactional(readOnly = true)
    public List<OrganizationalUnitResponseDTO> getAllUnits() {
        return unitRepository.findAll().stream()
                .map(unitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public OrganizationalUnitResponseDTO getUnitById(Long id) {
        OrganizationalUnit unit = unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found with ID: " + id));
        return unitMapper.toResponseDTO(unit);
    }

    // Get all units belonging to a specific type (e.g. "Get all Faculties")
    @Transactional(readOnly = true)
    public List<OrganizationalUnitResponseDTO> getUnitsByType(Long typeId) {
        return unitRepository.findByUnitTypeId(typeId).stream()
                .map(unitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Get hierarchy children (e.g. "Get all Departments in Faculty X")
    @Transactional(readOnly = true)
    public List<OrganizationalUnitResponseDTO> getUnitsByParent(Long parentId) {
        return unitRepository.findByParentId(parentId).stream()
                .map(unitMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public OrganizationalUnitResponseDTO updateUnit(Long id, OrganizationalUnitRequestDTO requestDTO) {
        OrganizationalUnit unit = unitRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unit not found ID: " + id));

        // Name Uniqueness check
        if (!unit.getName().equals(requestDTO.getName()) && unitRepository.existsByName(requestDTO.getName())) {
            throw new IllegalArgumentException("Unit Name already taken.");
        }

        // Update fields
        unitMapper.updateEntityFromDto(requestDTO, unit);

        // Update Type if changed
        if (!unit.getUnitType().getId().equals(requestDTO.getUnitTypeId())) {
            OrganizationalUnitType newType = typeRepository.findById(requestDTO.getUnitTypeId())
                    .orElseThrow(() -> new EntityNotFoundException("Unit Type not found"));
            unit.setUnitType(newType);
        }

        // Update Parent if changed
        if (requestDTO.getParentId() != null) {
            // Prevent self-referencing
            if (requestDTO.getParentId().equals(unit.getId())) {
                throw new IllegalArgumentException("Unit cannot be its own parent");
            }
            OrganizationalUnit newParent = unitRepository.findById(requestDTO.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent Unit not found"));
            unit.setParent(newParent);
        } else {
            // If parentId is explicitly null, remove parent
            unit.setParent(null);
        }

        OrganizationalUnit updatedUnit = unitRepository.save(unit);
        return unitMapper.toResponseDTO(updatedUnit);
    }

    @Transactional
    public void deleteUnit(Long id) {
        if (!unitRepository.existsById(id)) {
            throw new EntityNotFoundException("Unit not found ID: " + id);
        }
        // Check for children before deleting to prevent orphan chains
        // Alternatively, use cascade=REMOVE in entity, but manual check is safer for large orgs
        List<OrganizationalUnit> children = unitRepository.findByParentId(id);
        if (!children.isEmpty()) {
            throw new IllegalStateException("Cannot delete unit. It has " + children.size() + " child units. Delete children first.");
        }

        unitRepository.deleteById(id);
    }
}