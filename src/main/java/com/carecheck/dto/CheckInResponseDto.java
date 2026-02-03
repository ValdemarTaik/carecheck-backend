package com.carecheck.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CheckInResponseDto {
    private Long id;
    private Long wardId;
    private String wardName;
    private LocalDateTime checkInTime;
    private String status;
    private String notes;
    private String location;
}