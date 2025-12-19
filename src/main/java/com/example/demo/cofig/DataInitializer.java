package com.example.demo.cofig;

import com.example.demo.dto.user.CreateUserDto;
import com.example.demo.model.Category;
import com.example.demo.model.OrganizationalUnit;
import com.example.demo.model.OrganizationalUnitType;
import com.example.demo.model.Role;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.OrganizationalUnitRepository;
import com.example.demo.repository.OrganizationalUnitTypeRepository;
import com.example.demo.service.category.CategoryService;
import com.example.demo.service.user.OrganizationalUnitService;
import com.example.demo.service.user.OrganizationalUnitTypeService;
import com.example.demo.service.user.RoleService;
import com.example.demo.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleService roleService;
    private final UserService userService;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;
    private final OrganizationalUnitRepository unitRepository;
    private final OrganizationalUnitTypeRepository typeRepository;

    @Override
    public void run(String... args) throws Exception {
        initializeRoles();
        initializeDefaultAdmin();
        initializeCategories();
        initializeOrganizationalUnit();


    }



    private void initializeOrganizationalUnit() {


        OrganizationalUnitType uniType = createTypeIfNotFound("UNIVERSITY", "Institute/University Level");
        OrganizationalUnitType facultyType = createTypeIfNotFound("FACULTY", "Academic Faculty");
        OrganizationalUnitType deptType = createTypeIfNotFound("DEPARTMENT", "Academic Department/Chair");

        OrganizationalUnit bit = createUnitIfNotFound("Bahir Dar Institute of Technology (BiT)", "BiT", uniType, null);

        // Faculty of Computing
        OrganizationalUnit computing = createUnitIfNotFound("Faculty of Computing", "FoC", facultyType, bit);
        createUnitIfNotFound("Software Engineering", "SE", deptType, computing);
        createUnitIfNotFound("Computer Science", "CS", deptType, computing);
        createUnitIfNotFound("Information Technology", "IT", deptType, computing);
        createUnitIfNotFound("Information Systems", "IS", deptType, computing);
        createUnitIfNotFound("Cyber Security", "CSc", deptType, computing);

        // Faculty of Electrical
        OrganizationalUnit electrical = createUnitIfNotFound("Faculty of Electrical & Computer Engineering", "FECE", facultyType, bit);
        createUnitIfNotFound("Computer Engineering", "CE", deptType, electrical);
        createUnitIfNotFound("Electrical Power Engineering", "Power", deptType, electrical);

        // Faculty of Mechanical
        OrganizationalUnit mechanical = createUnitIfNotFound("Faculty of Mechanical & Industrial Engineering", "FMIE", facultyType, bit);
        createUnitIfNotFound("Mechanical Engineering", "Mech", deptType, mechanical);
        createUnitIfNotFound("Industrial Engineering", "Ind", deptType, mechanical);
        createUnitIfNotFound("Automotive Engineering", "Auto", deptType, mechanical);

        // Faculty of Civil
        OrganizationalUnit civil = createUnitIfNotFound("Faculty of Civil & Water Resource Engineering", "FCWRE", facultyType, bit);
        createUnitIfNotFound("Civil Engineering", "Civil", deptType, civil);
        createUnitIfNotFound("Hydraulic & Water Resources", "Hydraulic", deptType, civil);

        // Faculty of Chemical
        OrganizationalUnit chemical = createUnitIfNotFound("Faculty of Chemical & Food Engineering", "FCFE", facultyType, bit);
        createUnitIfNotFound("Chemical Engineering", "Chem", deptType, chemical);
        createUnitIfNotFound("Food Engineering", "Food", deptType, chemical);
    }


    private void initializeCategories() {
        createCategoryIfNotFound("Academic Affairs", "Grading, Exams, Classroom instruction issues");
        createCategoryIfNotFound("Facility & Infrastructure", "Broken equipment, Wi-Fi, Water, Dormitory issues");
        createCategoryIfNotFound("Administrative Service", "Delays, ID cards, Registrar issues");
        createCategoryIfNotFound("Financial", "Tuition, Cost sharing, Scholarship payments");
        createCategoryIfNotFound("Disciplinary & Conduct", "Harassment, Bullying, Student conflicts");

    }

    private void initializeRoles() {
        try {
            // Create ADMIN role
            if (!roleService.existsByName("ADMIN")) {
                roleService.createRole("ADMIN", "System Administrator with full access");
                log.info("Created ADMIN role");
            }

            // Create STAFF role (Complaint Officers)
            if (!roleService.existsByName("STAFF")) {
                roleService.createRole("STAFF", "Staff members who handle complaints");
                log.info("Created STAFF role");
            }

            // Create USER role (Students and other complainants)
            if (!roleService.existsByName("USER")) {
                roleService.createRole("USER", "Regular users who can submit complaints");
                log.info("Created USER role");
            }

        } catch (Exception e) {
            log.error("Error initializing roles: {}", e.getMessage());
        }
    }

    private void initializeDefaultAdmin() {
        try {
            // Create default admin user if not exists
            String adminEmail = "admin@university.edu";
            if (!userService.existsByEmail(adminEmail)) {
                Role adminRole = roleService.findByName("ADMIN")
                        .orElseThrow(() -> new RuntimeException("ADMIN role not found"));

                CreateUserDto adminDto = new CreateUserDto();
                adminDto.setFirstName("System");
                adminDto.setLastName("Administrator");
                adminDto.setEmail(adminEmail);
                adminDto.setPassword("admin123");
                adminDto.setRoleId(adminRole.getId());

                userService.createUser(adminDto);
                log.info("Created default admin user with email: {}", adminEmail);
                log.info("Default admin password: admin123 (Please change this in production!)");
            }

        } catch (Exception e) {
            log.error("Error initializing default admin: {}", e.getMessage());
        }
    }

    private OrganizationalUnitType createTypeIfNotFound(String name, String description) {
        return typeRepository.findAll().stream()
                .filter(t -> t.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElseGet(() -> typeRepository.save(OrganizationalUnitType.builder()
                        .name(name)
//                        .description(description)
//                        .status(1)
//                        .publicId(java.util.UUID.randomUUID().toString())//we will generate a UUID manually here
                        .build()));
    }

    private OrganizationalUnit createUnitIfNotFound(String name, String abbr, OrganizationalUnitType type, OrganizationalUnit parent) {
        if (!unitRepository.existsByName(name)) {
            OrganizationalUnit unit = OrganizationalUnit.builder()
                    .name(name)
//                    .abbreviation(abbr)
                    .unitType(type)
                    .parent(parent)
//                    .status(1)
//                    .publicId(java.util.UUID.randomUUID().toString())//here also we generate a UUID manually
                    .build();
            return unitRepository.save(unit);
        }
        return unitRepository.findAll().stream()
                .filter(u -> u.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    private void createCategoryIfNotFound(String name, String description) {
        if (!categoryRepository.existsByName(name)) {
            categoryRepository.save(Category.builder().name(name).build());
        }
    }
}