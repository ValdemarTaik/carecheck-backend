package com.carecheck.service;

import com.carecheck.entity.Relative;
import com.carecheck.entity.User;
import com.carecheck.entity.Ward;
import com.carecheck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RelationshipService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Добавить подопечного к родственнику
     */
    public void addWardToRelative(Long relativeId, Long wardId) {
        User relativeUser = userRepository.findById(relativeId)
                .orElseThrow(() -> new RuntimeException("Relative not found"));

        User wardUser = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"RELATIVE".equals(relativeUser.getUserType())) {
            throw new RuntimeException("User is not a Relative");
        }

        if (!"WARD".equals(wardUser.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        Relative relative = (Relative) relativeUser;
        Ward ward = (Ward) wardUser;

        // Проверяем, не добавлен ли уже этот подопечный
        if (relative.getWards().contains(ward)) {
            throw new RuntimeException("Ward is already assigned to this relative");
        }

        // Добавляем подопечного
        relative.addWard(ward);
        userRepository.save(relative);
    }

    /**
     * Удалить подопечного у родственника
     */
    public void removeWardFromRelative(Long relativeId, Long wardId) {
        User relativeUser = userRepository.findById(relativeId)
                .orElseThrow(() -> new RuntimeException("Relative not found"));

        User wardUser = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"RELATIVE".equals(relativeUser.getUserType())) {
            throw new RuntimeException("User is not a Relative");
        }

        if (!"WARD".equals(wardUser.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        Relative relative = (Relative) relativeUser;
        Ward ward = (Ward) wardUser;

        // Удаляем подопечного
        relative.removeWard(ward);
        userRepository.save(relative);
    }

    /**
     * Получить всех подопечных родственника
     */
    public List<Map<String, Object>> getRelativeWards(Long relativeId) {
        User relativeUser = userRepository.findById(relativeId)
                .orElseThrow(() -> new RuntimeException("Relative not found"));

        if (!"RELATIVE".equals(relativeUser.getUserType())) {
            throw new RuntimeException("User is not a Relative");
        }

        Relative relative = (Relative) relativeUser;

        return relative.getWards().stream()
                .map(this::convertWardToMap)
                .collect(Collectors.toList());
    }

    /**
     * Получить всех родственников подопечного
     */
    public List<Map<String, Object>> getWardRelatives(Long wardId) {
        User wardUser = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"WARD".equals(wardUser.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        Ward ward = (Ward) wardUser;

        // Получаем всех пользователей и фильтруем родственников
        return userRepository.findAll().stream()
                .filter(user -> "RELATIVE".equals(user.getUserType()))
                .map(user -> (Relative) user)
                .filter(relative -> relative.getWards().contains(ward))
                .map(this::convertRelativeToMap)
                .collect(Collectors.toList());
    }

    /**
     * Поиск подопечных по имени или email (для добавления)
     */
    public List<Map<String, Object>> searchWards(String searchTerm) {
        return userRepository.findAll().stream()
                .filter(user -> "WARD".equals(user.getUserType()))
                .filter(user -> user.getName().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(searchTerm.toLowerCase()) ||
                        user.getUsername().toLowerCase().contains(searchTerm.toLowerCase()))
                .map(user -> {
                    Ward ward = (Ward) user;
                    Map<String, Object> map = convertWardToMap(ward);
                    map.put("email", ward.getEmail());
                    map.put("phoneNumber", ward.getPhoneNumber());
                    return map;
                })
                .collect(Collectors.toList());
    }

    /**
     * Преобразование Ward в Map для JSON
     */
    private Map<String, Object> convertWardToMap(Ward ward) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", ward.getId());
        map.put("name", ward.getName());
        map.put("username", ward.getUsername());
        map.put("lastCheckIn", ward.getLastCheckIn());
        map.put("checkIntervalHours", ward.getCheckIntervalHours());
        map.put("notificationsEnabled", ward.getNotificationsEnabled());
        map.put("address", ward.getAddress());
        return map;
    }

    /**
     * Преобразование Relative в Map для JSON
     */
    private Map<String, Object> convertRelativeToMap(Relative relative) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", relative.getId());
        map.put("name", relative.getName());
        map.put("email", relative.getEmail());
        map.put("phoneNumber", relative.getPhoneNumber());
        map.put("receiveEmailNotifications", relative.getReceiveEmailNotifications());
        map.put("receiveSmsNotifications", relative.getReceiveSmsNotifications());
        return map;
    }

    /**
     * Получить дашборд для родственника
     */
    public Map<String, Object> getRelativeDashboard(Long relativeId) {
        User relativeUser = userRepository.findById(relativeId)
                .orElseThrow(() -> new RuntimeException("Relative not found"));

        if (!"RELATIVE".equals(relativeUser.getUserType())) {
            throw new RuntimeException("User is not a Relative");
        }

        Relative relative = (Relative) relativeUser;
        Map<String, Object> dashboard = new HashMap<>();

        // Основная информация о родственнике
        dashboard.put("relativeId", relative.getId());
        dashboard.put("relativeName", relative.getName());
        dashboard.put("email", relative.getEmail());
        dashboard.put("phoneNumber", relative.getPhoneNumber());

        // Получаем всех подопечных с их статусами
        List<Map<String, Object>> wardsWithStatus = new ArrayList<>();

        for (Ward ward : relative.getWards()) {
            Map<String, Object> wardInfo = convertWardToMap(ward);

            // Добавляем статус (можно вызвать CheckInService)
            wardInfo.put("status", getWardStatus(ward));

            wardsWithStatus.add(wardInfo);
        }

        dashboard.put("wards", wardsWithStatus);
        dashboard.put("totalWards", relative.getWards().size());

        // Статистика
        long okCount = wardsWithStatus.stream()
                .filter(w -> "OK".equals(w.get("status")))
                .count();
        long warningCount = wardsWithStatus.stream()
                .filter(w -> "WARNING".equals(w.get("status")))
                .count();
        long dangerCount = wardsWithStatus.stream()
                .filter(w -> "DANGER".equals(w.get("status")))
                .count();

        dashboard.put("stats", Map.of(
                "ok", okCount,
                "warning", warningCount,
                "danger", dangerCount
        ));

        return dashboard;
    }

    /**
     * Определить статус подопечного
     */
    private String getWardStatus(Ward ward) {
        if (ward.getLastCheckIn() == null) {
            return "NO_CHECKINS";
        }

        long hoursSinceLastCheck = java.time.Duration.between(
                ward.getLastCheckIn(),
                java.time.LocalDateTime.now()
        ).toHours();

        if (hoursSinceLastCheck <= ward.getCheckIntervalHours()) {
            return "OK";
        } else if (hoursSinceLastCheck <= ward.getCheckIntervalHours() * 2) {
            return "WARNING";
        } else {
            return "DANGER";
        }
    }
}