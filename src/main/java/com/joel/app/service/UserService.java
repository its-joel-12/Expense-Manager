package com.joel.app.service;

import com.joel.app.dto.UserRequestDto;
import com.joel.app.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    List<UserResponseDto> getAllUsers();

    UserResponseDto createUser(UserRequestDto userRequestDto);
}
