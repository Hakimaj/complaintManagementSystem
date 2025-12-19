package com.example.demo.service.user.userserviceimpl;

import com.example.demo.dto.user.OrganizationalUnitTypeDto;
import com.example.demo.mapper.OrganizationalUnitTypeMapper;
import com.example.demo.model.OrganizationalUnitType;
import com.example.demo.repository.OrganizationalUnitTypeRepository;
import com.example.demo.service.user.OrganizationalUnitTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationalUnitTypeServiceImpl implements OrganizationalUnitTypeService {

    private final OrganizationalUnitTypeRepository organizationalUnitTypeRepository;
    private final OrganizationalUnitTypeMapper organizationalUnitTypeMapper;

    // Entity methods
    @Override
    public OrganizationalUnitType createOrganizationalUnitType(String name) {
        if (existsByName(name)) {
            throw new RuntimeException("Organizational unit type already exists with name: " + name);
        }

        OrganizationalUnitType unitType = new OrganizationalUnitType();
        unitType.setName(name);
        return organizationalUnitTypeRepository.save(unitType);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationalUnitType> findById(Long id) {
        return organizationalUnitTypeRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationalUnitType> findByName(String name) {
        return organizationalUnitTypeRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationalUnitType> findAll() {
        return organizationalUnitTypeRepository.findAll();
    }

    @Override
    public OrganizationalUnitType updateOrganizationalUnitType(Long id, String name) {
        OrganizationalUnitType unitType = organizationalUnitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organizational unit type not found with id: " + id));

        if (!unitType.getName().equals(name) && existsByName(name)) {
            throw new RuntimeException("Organizational unit type already exists with name: " + name);
        }

        unitType.setName(name);
        return organizationalUnitTypeRepository.save(unitType);
    }

    @Override
    public void deleteOrganizationalUnitType(Long id) {
        if (!organizationalUnitTypeRepository.existsById(id)) {
            throw new RuntimeException("Organizational unit type not found with id: " + id);
        }
        organizationalUnitTypeRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return organizationalUnitTypeRepository.existsByName(name);
    }

    // DTO methods
    @Override
    public OrganizationalUnitTypeDto createOrganizationalUnitTypeDto(OrganizationalUnitTypeDto organizationalUnitTypeDto) {
        if (existsByName(organizationalUnitTypeDto.getName())) {
            throw new RuntimeException("Organizational unit type already exists with name: " + organizationalUnitTypeDto.getName());
        }

        OrganizationalUnitType unitType = organizationalUnitTypeMapper.toEntity(organizationalUnitTypeDto);
        OrganizationalUnitType savedUnitType = organizationalUnitTypeRepository.save(unitType);
        return organizationalUnitTypeMapper.toDto(savedUnitType);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationalUnitTypeDto> findByIdDto(Long id) {
        return organizationalUnitTypeRepository.findById(id).map(organizationalUnitTypeMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationalUnitTypeDto> findAllDto() {
        return organizationalUnitTypeRepository.findAll().stream()
                .map(organizationalUnitTypeMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public OrganizationalUnitTypeDto updateOrganizationalUnitTypeDto(Long id, OrganizationalUnitTypeDto organizationalUnitTypeDto) {
        OrganizationalUnitType unitType = organizationalUnitTypeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organizational unit type not found with id: " + id));

        if (!unitType.getName().equals(organizationalUnitTypeDto.getName()) && existsByName(organizationalUnitTypeDto.getName())) {
            throw new RuntimeException("Organizational unit type already exists with name: " + organizationalUnitTypeDto.getName());
        }

        organizationalUnitTypeMapper.updateEntityFromDto(organizationalUnitTypeDto, unitType);
        OrganizationalUnitType updatedUnitType = organizationalUnitTypeRepository.save(unitType);
        return organizationalUnitTypeMapper.toDto(updatedUnitType);
    }
}