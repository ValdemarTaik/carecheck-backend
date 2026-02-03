package com.carecheck.dto;

import lombok.Data;

@Data
public class UserRegistrationDto {
    private String username;
    private String email;
    private String password;
    private String name;
    private String phoneNumber;
    private String userType;
    private Integer checkIntervalHours = 24;
    private Boolean notificationsEnabled = true;
    private String address;
    private String medicalNotes;
    private Boolean receiveEmailNotifications = true;
    private Boolean receiveSmsNotifications = false;
    private Integer notificationDelayMinutes = 0;
}