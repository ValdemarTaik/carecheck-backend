package com.carecheck.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserResponseDto {
    private Long id;
    private String username;
    private String email;
    private String name;
    private String phoneNumber;
    private String userType;
    private LocalDateTime createdAt;
    private LocalDateTime lastCheckIn;
    private Integer checkIntervalHours;
    private Boolean notificationsEnabled;
}