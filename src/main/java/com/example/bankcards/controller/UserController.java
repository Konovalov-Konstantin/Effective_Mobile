package com.example.bankcards.controller;

import com.example.bankcards.dto.CreateOrUpdateUserRequest;
import com.example.bankcards.dto.UserDto;
import com.example.bankcards.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDto> createUser(@RequestBody CreateOrUpdateUserRequest request) {
        return ResponseEntity.ok(userService.createUser(request));
    }

    @PutMapping("/{userId}/updateUser")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long userId, @RequestBody CreateOrUpdateUserRequest request) {
        return ResponseEntity.ok(userService.updateUser(userId, request));
    }

    @DeleteMapping("/{userId}/delete")
    public ResponseEntity<Void> deleteCard(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.ok().build();
    }
}
