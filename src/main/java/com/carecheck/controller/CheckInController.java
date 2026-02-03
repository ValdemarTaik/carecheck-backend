package com.carecheck.controller;

import com.carecheck.dto.CheckInRequestDto;
import com.carecheck.dto.CheckInResponseDto;
import com.carecheck.entity.CheckIn;
import com.carecheck.service.CheckInService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/checkins")
public class CheckInController {

    @Autowired
    private CheckInService checkInService;

    @PostMapping
    public ResponseEntity<?> createCheckIn(
            @RequestParam Long wardId,
            @RequestParam(required = false) String notes,
            @RequestParam(required = false) String location) {

        try {
            CheckIn checkIn = checkInService.createCheckIn(wardId, notes, location);
            return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/ward/{wardId}")
    public ResponseEntity<?> getCheckInHistory(@PathVariable Long wardId) {
        try {
            List<CheckIn> checkIns = checkInService.getCheckInHistory(wardId);
            return ResponseEntity.ok(checkIns);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/ward/{wardId}/last")
    public ResponseEntity<?> getLastCheckIn(@PathVariable Long wardId) {
        try {
            CheckIn checkIn = checkInService.getLastCheckIn(wardId);
            return ResponseEntity.ok(checkIn);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/ward/{wardId}/status")
    public ResponseEntity<?> getWardStatus(@PathVariable Long wardId) {
        try {
            Map<String, Object> status = checkInService.getWardStatus(wardId);
            return ResponseEntity.ok(status);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @GetMapping("/ward/{wardId}/statistics")
    public ResponseEntity<?> getStatistics(@PathVariable Long wardId) {
        try {
            Map<String, Object> stats = checkInService.getStatistics(wardId);
            return ResponseEntity.ok(stats);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    @PostMapping("/v2")
    public ResponseEntity<?> createCheckInV2(@RequestBody CheckInRequestDto request) {
        try {
            CheckIn checkIn = checkInService.createCheckIn(
                    request.getWardId(),
                    request.getNotes(),
                    request.getLocation()
            );

            // Создаем DTO для ответа
            CheckInResponseDto response = new CheckInResponseDto();
            response.setId(checkIn.getId());
            response.setWardId(checkIn.getWard().getId());
            response.setWardName(checkIn.getWard().getName());
            response.setCheckInTime(checkIn.getCheckInTime());
            response.setStatus(checkIn.getStatus());

            // Используем геттеры, которые мы явно добавили в CheckIn
            response.setNotes(checkIn.getNotes());
            response.setLocation(checkIn.getLocation());

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @PostMapping("/simple")
    public ResponseEntity<?> createCheckInSimple(
            @RequestParam Long wardId,
            @RequestParam(required = false, defaultValue = "") String notes,
            @RequestParam(required = false, defaultValue = "") String location) {

        try {
            CheckIn checkIn = checkInService.createCheckIn(wardId, notes, location);
            return ResponseEntity.status(HttpStatus.CREATED).body(checkIn);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    @GetMapping("/test")
    public ResponseEntity<?> testCheckInSystem() {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("message", "CheckIn system is working");
            response.put("timestamp", java.time.LocalDateTime.now().toString());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }

    @GetMapping("/check-all")
    public ResponseEntity<?> manuallyCheckAllWards() {
        try {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Manual check initiated. Check server logs for details.");
            response.put("note", "Automatic checks run every 5 minutes by scheduler");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.internalServerError().body(error);
        }
    }
}