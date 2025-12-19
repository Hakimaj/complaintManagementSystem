package com.example.demo.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
    private String token;
    private String type = "Bearer";
    private UserDto user;

    public AuthResponseDto(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }
}