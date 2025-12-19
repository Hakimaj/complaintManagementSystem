package com.example.demo.service.user;

import com.example.demo.dto.user.AuthResponseDto;
import com.example.demo.dto.user.LoginDto;
import com.example.demo.dto.user.RegisterDto;
import com.example.demo.dto.user.UserDto;

public interface AuthService {

    AuthResponseDto login(LoginDto loginDto);

    UserDto register(RegisterDto registerDto);

    UserDto getCurrentUser();

    void logout();
}