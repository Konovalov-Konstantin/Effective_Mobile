package com.example.bankcards.service;

import com.example.bankcards.dto.CreateOrUpdateUserRequest;
import com.example.bankcards.dto.UserDto;

public interface UserService {
    UserDto getUserById(Long userId);
    UserDto createUser(CreateOrUpdateUserRequest request);
    UserDto updateUser(Long userId, CreateOrUpdateUserRequest request);
    void deleteUser(Long userId);
}
