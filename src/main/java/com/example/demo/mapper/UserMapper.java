package com.example.demo.mapper;

import com.example.demo.dto.user.CreateUserDto;
import com.example.demo.dto.user.RegisterDto;
import com.example.demo.dto.user.UserDto;
import com.example.demo.model.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {RoleMapper.class, OrganizationalUnitMapper.class})
public interface UserMapper {

    @Mapping(source = "role.name", target = "roleName")
    @Mapping(source = "organizationalUnit.name", target = "organizationalUnitName")
    UserDto toDto(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true) // Will be set manually in service
    @Mapping(target = "organizationalUnit", ignore = true) // Will be set manually in service
    @Mapping(target = "complaints", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toEntity(CreateUserDto createUserDto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true) // Will be set to USER role in service
    @Mapping(target = "organizationalUnit", ignore = true)
    @Mapping(target = "complaints", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "isActive", constant = "true")
    User toEntity(RegisterDto registerDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true) // Will be handled manually if roleId is provided
    @Mapping(target = "organizationalUnit", ignore = true) // Will be handled manually if organizationalUnitId is provided
    @Mapping(target = "complaints", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "password", ignore = true) // Password updates handled separately
    void updateEntityFromDto(com.example.demo.dto.user.UpdateUserDto updateUserDto, @MappingTarget User user);
}