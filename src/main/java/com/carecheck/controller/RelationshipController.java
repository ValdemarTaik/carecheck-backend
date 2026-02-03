package com.carecheck.controller;

import com.carecheck.service.RelationshipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/relationships")
public class RelationshipController {

    @Autowired
    private RelationshipService relationshipService;

    /**
     * Добавить подопечного к родственнику
     * POST /api/relationships/add
     */
    @PostMapping("/add")
    public ResponseEntity<?> addWardToRelative(
            @RequestParam Long relativeId,
            @RequestParam Long wardId) {

        try {
            relationshipService.addWardToRelative(relativeId, wardId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Ward successfully added to relative");
            response.put("relativeId", String.valueOf(relativeId));
            response.put("wardId", String.valueOf(wardId));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Удалить подопечного у родственника
     * DELETE /api/relationships/remove
     */
    @DeleteMapping("/remove")
    public ResponseEntity<?> removeWardFromRelative(
            @RequestParam Long relativeId,
            @RequestParam Long wardId) {

        try {
            relationshipService.removeWardFromRelative(relativeId, wardId);

            Map<String, String> response = new HashMap<>();
            response.put("message", "Ward successfully removed from relative");
            response.put("relativeId", String.valueOf(relativeId));
            response.put("wardId", String.valueOf(wardId));

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Получить всех подопечных родственника
     * GET /api/relationships/relative/{relativeId}/wards
     */
    @GetMapping("/relative/{relativeId}/wards")
    public ResponseEntity<?> getRelativeWards(@PathVariable Long relativeId) {
        try {
            return ResponseEntity.ok(relationshipService.getRelativeWards(relativeId));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Получить всех родственников подопечного
     * GET /api/relationships/ward/{wardId}/relatives
     */
    @GetMapping("/ward/{wardId}/relatives")
    public ResponseEntity<?> getWardRelatives(@PathVariable Long wardId) {
        try {
            return ResponseEntity.ok(relationshipService.getWardRelatives(wardId));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Поиск подопечных для добавления
     * GET /api/relationships/search/wards
     */
    @GetMapping("/search/wards")
    public ResponseEntity<?> searchWards(@RequestParam String query) {
        try {
            return ResponseEntity.ok(relationshipService.searchWards(query));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }

    /**
     * Получить дашборд родственника
     * GET /api/relationships/relative/{relativeId}/dashboard
     */
    @GetMapping("/relative/{relativeId}/dashboard")
    public ResponseEntity<?> getRelativeDashboard(@PathVariable Long relativeId) {
        try {
            return ResponseEntity.ok(relationshipService.getRelativeDashboard(relativeId));
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }

    /**
     * Тестовый эндпоинт - связать тестовых пользователей
     * GET /api/relationships/test-setup
     */
    @GetMapping("/test-setup")
    public ResponseEntity<?> setupTestRelationships() {
        try {
            // Связываем тестового родственника (ID=2) с тестовым подопечным (ID=1)
            relationshipService.addWardToRelative(2L, 1L);

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Test relationships setup successfully");
            response.put("relativeId", 2);
            response.put("wardId", 1);
            response.put("note", "Relative (son_alex) now watches Ward (grandma_anna)");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        }
    }
}