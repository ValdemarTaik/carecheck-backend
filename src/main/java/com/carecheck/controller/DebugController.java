package com.carecheck.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @GetMapping("/status")
    public Map<String, String> status() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "CareCheck API is running");
        response.put("timestamp", String.valueOf(System.currentTimeMillis()));
        response.put("version", "1.0.0");
        return response;
    }

    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "CareCheck Backend");
        response.put("timestamp", System.currentTimeMillis());
        response.put("components", Map.of(
                "database", "PostgreSQL",
                "cache", "none",
                "security", "disabled"
        ));
        return response;
    }

    @GetMapping("/info")
    public Map<String, String> info() {
        Map<String, String> info = new HashMap<>();
        info.put("application", "CareCheck");
        info.put("description", "System for monitoring elderly people");
        info.put("author", "Your Name");
        info.put("github", "https://github.com/yourusername/carecheck");
        return info;
    }
}