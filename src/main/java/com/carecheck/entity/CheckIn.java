package com.carecheck.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "check_ins")
public class CheckIn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "ward_id", nullable = false)
    private User ward;

    @Column(name = "check_in_time", nullable = false)
    private LocalDateTime checkInTime;

    @Column(name = "status", nullable = false)
    private String status = "OK";

    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "location")
    private String location;

    @PrePersist
    protected void onCreate() {
        if (checkInTime == null) {
            checkInTime = LocalDateTime.now();
        }
    }

    // Явно добавляем геттеры и сеттеры для notes и location
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}