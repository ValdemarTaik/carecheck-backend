package com.carecheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type", discriminatorType = DiscriminatorType.STRING)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "user_type", insertable = false, updatable = false)
    private String userType;

    // Поля для Ward
    @Column(name = "last_check_in")
    private LocalDateTime lastCheckIn;

    @Column(name = "check_interval_hours")
    private Integer checkIntervalHours;

    @Column(name = "notifications_enabled")
    private Boolean notificationsEnabled;

    @Column(name = "address")
    private String address;

    @Column(name = "medical_notes", columnDefinition = "TEXT")
    private String medicalNotes;

    // Поля для Relative
    @Column(name = "receive_email_notifications")
    private Boolean receiveEmailNotifications;

    @Column(name = "receive_sms_notifications")
    private Boolean receiveSmsNotifications;

    @Column(name = "notification_delay_minutes")
    private Integer notificationDelayMinutes;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}