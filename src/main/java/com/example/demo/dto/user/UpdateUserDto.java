package com.example.demo.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserDto {

    @Size(max = 30, message = "First name must not exceed 30 characters")
    private String firstName;

    @Size(max = 30, message = "Last name must not exceed 30 characters")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 50, message = "Email must not exceed 50 characters")
    private String email;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    private String phoneNumber;

    @Size(max = 20, message = "Student ID must not exceed 20 characters")
    private String studentId;

    private Boolean isActive;

    private Long roleId;

    private Long organizationalUnitId;
}