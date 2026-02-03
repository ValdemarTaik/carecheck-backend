package com.carecheck.controller;

import com.carecheck.dto.UserRegistrationDto;
import com.carecheck.dto.UserResponseDto;
import com.carecheck.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // ========== GET endpoints without path variables FIRST ==========

    /**
     * Create test users (WARD and RELATIVE)
     * GET /api/users/test-data
     */
    @GetMapping("/test-data")
    public ResponseEntity<?> createTestData() {
        try {
            // Create test ward
            UserRegistrationDto wardDto = new UserRegistrationDto();
            wardDto.setUsername("grandma_anna");
            wardDto.setEmail("anna@example.com");
            wardDto.setPassword("password123");
            wardDto.setName("Anna Petrova");
            wardDto.setPhoneNumber("+375291234567");
            wardDto.setUserType("WARD");
            wardDto.setAddress("Minsk, Pushkina 1");
            wardDto.setCheckIntervalHours(24);

            UserResponseDto ward = userService.registerUser(wardDto);

            // Create test relative
            UserRegistrationDto relativeDto = new UserRegistrationDto();
            relativeDto.setUsername("son_alex");
            relativeDto.setEmail("alex@example.com");
            relativeDto.setPassword("password123");
            relativeDto.setName("Alex Ivanov");
            relativeDto.setPhoneNumber("+375297654321");
            relativeDto.setUserType("RELATIVE");

            UserResponseDto relative = userService.registerUser(relativeDto);

            Map<String, Object> response = new HashMap<>();
            response.put("ward", ward);
            response.put("relative", relative);
            response.put("message", "Test data created successfully");
            response.put("note", "Ward ID: " + ward.getId() + ", Relative ID: " + relative.getId());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Get total user count
     * GET /api/users/count
     */
    @GetMapping("/count")
    public ResponseEntity<Map<String, Long>> getUserCount() {
        long count = userService.countUsers();
        Map<String, Long> response = new HashMap<>();
        response.put("count", count);
        return ResponseEntity.ok(response);
    }

    // ========== GET endpoints with path variables SECOND ==========

    /**
     * Get user by ID
     * GET /api/users/{id}
     * Note: {id:\\d+} means only numeric IDs
     */
    @GetMapping("/{id:\\d+}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            UserResponseDto user = userService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Get user by username
     * GET /api/users/username/{username}
     */
    @GetMapping("/username/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        try {
            UserResponseDto user = userService.getUserByUsername(username);
            return ResponseEntity.ok(user);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    // ========== POST endpoints ==========

    /**
     * Register new user
     * POST /api/users/register
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            UserResponseDto user = userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(user);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Quick test endpoint
     * GET /api/users/ping
     */
    @GetMapping("/ping")
    public ResponseEntity<Map<String, String>> ping() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "User API is working");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        return ResponseEntity.ok(response);
    }
}