package com.carecheck.dto;

import lombok.Data;

@Data
public class CheckInRequestDto {
    private Long wardId;
    private String notes;
    private String location;
}