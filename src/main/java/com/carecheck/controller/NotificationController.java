package com.carecheck.controller;

import com.carecheck.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    /**
     * Тестовая отправка уведомления
     * GET /api/notifications/test
     */
    @GetMapping("/test")
    public ResponseEntity<?> testNotification() {
        try {
            notificationService.sendTestNotification();

            Map<String, String> response = new HashMap<>();
            response.put("message", "Test notification sent. Check server logs.");
            response.put("timestamp", java.time.LocalDateTime.now().toString());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Получить статус системы уведомлений
     * GET /api/notifications/status
     */
    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getNotificationStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("system", "Notification System");
        status.put("status", "ACTIVE");
        status.put("features", new String[] {
                "Missed check-in notifications",
                "Check-in confirmation",
                "Console logging",
                "Future: Email/SMS/Push"
        });
        status.put("timestamp", System.currentTimeMillis());

        return ResponseEntity.ok(status);
    }
}