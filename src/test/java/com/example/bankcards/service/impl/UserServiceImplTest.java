package com.example.bankcards.service.impl;

import com.example.bankcards.dto.CreateOrUpdateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.entity.Role;
import com.example.bankcards.entity.User;
import com.example.bankcards.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private User testUser;
    private CreateOrUpdateUserRequest createRequest;
    private CreateOrUpdateUserRequest updateRequest;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("testuser");
        testUser.setPassword("encoded_password");
        testUser.setRole(Role.USER);
        testUser.setCards(new ArrayList<>());

        createRequest = new CreateOrUpdateUserRequest();
        createRequest.setUsername("newuser");
        createRequest.setPassword("password123");
        createRequest.setRole(Role.USER.name());

        updateRequest = new CreateOrUpdateUserRequest();
        updateRequest.setUsername("updateduser");
        updateRequest.setPassword("newpassword");
        updateRequest.setRole(Role.ADMIN.name());
    }


    @Test
    void shouldReturnUserById() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        UserDto result = userService.getUserById(1L);
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("USER", result.getRole());
        assertEquals(0, result.getCards().size());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    void shouldUpdateUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newpassword")).thenReturn("new_encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(testUser);
        UserDto updatedUser = userService.updateUser(1L, updateRequest);
        assertNotNull(updatedUser);
        assertEquals(updatedUser.getUsername(), testUser.getUsername());
        assertEquals(Role.valueOf(updatedUser.getRole()), testUser.getRole());
        assertEquals("new_encoded_password", testUser.getPassword());

    }
}