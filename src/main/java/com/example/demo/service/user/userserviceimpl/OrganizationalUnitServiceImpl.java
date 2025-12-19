package com.example.demo.service.user.userserviceimpl;

import com.example.demo.model.OrganizationalUnit;
import com.example.demo.model.OrganizationalUnitType;
import com.example.demo.repository.OrganizationalUnitRepository;
import com.example.demo.repository.OrganizationalUnitTypeRepository;
import com.example.demo.service.user.OrganizationalUnitService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrganizationalUnitServiceImpl implements OrganizationalUnitService {

    private final OrganizationalUnitRepository organizationalUnitRepository;
    private final OrganizationalUnitTypeRepository organizationalUnitTypeRepository;

    @Override
    public OrganizationalUnit createOrganizationalUnit(String name, Long parentId, Long unitTypeId) {
        if (existsByName(name)) {
            throw new RuntimeException("Organizational unit already exists with name: " + name);
        }

        OrganizationalUnit unit = new OrganizationalUnit();
        unit.setName(name);

        if (parentId != null) {
            OrganizationalUnit parent = organizationalUnitRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent organizational unit not found with id: " + parentId));
            unit.setParent(parent);
        }

        OrganizationalUnitType unitType = organizationalUnitTypeRepository.findById(unitTypeId)
                .orElseThrow(() -> new RuntimeException("Organizational unit type not found with id: " + unitTypeId));
        unit.setUnitType(unitType);

        return organizationalUnitRepository.save(unit);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationalUnit> findById(Long id) {
        return organizationalUnitRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<OrganizationalUnit> findByName(String name) {
        return organizationalUnitRepository.findByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationalUnit> findAll() {
        return organizationalUnitRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationalUnit> findByParentId(Long parentId) {
        return organizationalUnitRepository.findByParentId(parentId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrganizationalUnit> findByUnitTypeId(Long unitTypeId) {
        return organizationalUnitRepository.findByUnitTypeId(unitTypeId);
    }

    @Override
    public OrganizationalUnit updateOrganizationalUnit(Long id, String name, Long parentId, Long unitTypeId) {
        OrganizationalUnit unit = organizationalUnitRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organizational unit not found with id: " + id));

        if (!unit.getName().equals(name) && existsByName(name)) {
            throw new RuntimeException("Organizational unit already exists with name: " + name);
        }

        unit.setName(name);

        if (parentId != null) {
            OrganizationalUnit parent = organizationalUnitRepository.findById(parentId)
                    .orElseThrow(() -> new RuntimeException("Parent organizational unit not found with id: " + parentId));
            unit.setParent(parent);
        } else {
            unit.setParent(null);
        }

        OrganizationalUnitType unitType = organizationalUnitTypeRepository.findById(unitTypeId)
                .orElseThrow(() -> new RuntimeException("Organizational unit type not found with id: " + unitTypeId));
        unit.setUnitType(unitType);

        return organizationalUnitRepository.save(unit);
    }

    @Override
    public void deleteOrganizationalUnit(Long id) {
        if (!organizationalUnitRepository.existsById(id)) {
            throw new RuntimeException("Organizational unit not found with id: " + id);
        }
        organizationalUnitRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return organizationalUnitRepository.existsByName(name);
    }
}
