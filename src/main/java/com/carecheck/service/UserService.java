package com.carecheck.service;

import com.carecheck.dto.UserRegistrationDto;
import com.carecheck.dto.UserResponseDto;
import com.carecheck.entity.Relative;
import com.carecheck.entity.User;
import com.carecheck.entity.Ward;
import com.carecheck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new RuntimeException("User with this email already exists");
        }

        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new RuntimeException("User with this username already exists");
        }

        User user;

        if ("WARD".equalsIgnoreCase(registrationDto.getUserType())) {
            Ward ward = new Ward();
            ward.setUsername(registrationDto.getUsername());
            ward.setEmail(registrationDto.getEmail());
            ward.setPassword(registrationDto.getPassword());
            ward.setName(registrationDto.getName());
            ward.setPhoneNumber(registrationDto.getPhoneNumber());
            ward.setCheckIntervalHours(registrationDto.getCheckIntervalHours());
            ward.setNotificationsEnabled(registrationDto.getNotificationsEnabled());
            ward.setAddress(registrationDto.getAddress());
            ward.setMedicalNotes(registrationDto.getMedicalNotes());
            user = ward;

        } else if ("RELATIVE".equalsIgnoreCase(registrationDto.getUserType())) {
            Relative relative = new Relative();
            relative.setUsername(registrationDto.getUsername());
            relative.setEmail(registrationDto.getEmail());
            relative.setPassword(registrationDto.getPassword());
            relative.setName(registrationDto.getName());
            relative.setPhoneNumber(registrationDto.getPhoneNumber());
            relative.setReceiveEmailNotifications(registrationDto.getReceiveEmailNotifications());
            relative.setReceiveSmsNotifications(registrationDto.getReceiveSmsNotifications());
            relative.setNotificationDelayMinutes(registrationDto.getNotificationDelayMinutes());
            user = relative;

        } else {
            throw new RuntimeException("Invalid user type. Must be WARD or RELATIVE");
        }

        User savedUser = userRepository.save(user);
        return convertToDto(savedUser);
    }

    public UserResponseDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    public UserResponseDto getUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return convertToDto(user);
    }

    private UserResponseDto convertToDto(User user) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setUserType(user.getUserType());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setLastCheckIn(user.getLastCheckIn());
        dto.setCheckIntervalHours(user.getCheckIntervalHours());
        dto.setNotificationsEnabled(user.getNotificationsEnabled());
        return dto;
    }

    public long countUsers() {
        return userRepository.count();
    }
}