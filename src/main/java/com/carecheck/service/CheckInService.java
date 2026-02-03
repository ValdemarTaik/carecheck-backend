package com.carecheck.service;

import com.carecheck.entity.CheckIn;
import com.carecheck.entity.User;
import com.carecheck.entity.Ward;
import com.carecheck.repository.CheckInRepository;
import com.carecheck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CheckInService {

    @Autowired
    private CheckInRepository checkInRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationService notificationService;

    public CheckIn createCheckIn(Long wardId, String notes, String location) {
        User wardUser = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found with id: " + wardId));

        if (!"WARD".equals(wardUser.getUserType())) {
            throw new RuntimeException("User is not a Ward. User type: " + wardUser.getUserType());
        }

        Ward ward = (Ward) wardUser;

        CheckIn checkIn = new CheckIn();
        checkIn.setWard(ward);
        checkIn.setNotes(notes);
        checkIn.setLocation(location);
        checkIn.setStatus("OK");

        CheckIn savedCheckIn = checkInRepository.save(checkIn);

        ward.setLastCheckIn(LocalDateTime.now());
        userRepository.save(ward);

        notificationService.sendCheckInNotification(ward);

        return savedCheckIn;
    }

    public List<CheckIn> getCheckInHistory(Long wardId) {
        User ward = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"WARD".equals(ward.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        return checkInRepository.findByWardOrderByCheckInTimeDesc(ward);
    }

    public CheckIn getLastCheckIn(Long wardId) {
        User ward = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"WARD".equals(ward.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        return checkInRepository.findFirstByWardOrderByCheckInTimeDesc(ward)
                .orElseThrow(() -> new RuntimeException("No check-ins found for this ward"));
    }

    public Map<String, Object> getWardStatus(Long wardId) {
        User wardUser = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"WARD".equals(wardUser.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        Ward ward = (Ward) wardUser;
        Map<String, Object> status = new HashMap<>();

        status.put("wardId", ward.getId());
        status.put("wardName", ward.getName());
        status.put("checkIntervalHours", ward.getCheckIntervalHours());
        status.put("notificationsEnabled", ward.getNotificationsEnabled());

        try {
            CheckIn lastCheckIn = getLastCheckIn(wardId);
            status.put("lastCheckIn", lastCheckIn.getCheckInTime());
            status.put("lastCheckInId", lastCheckIn.getId());
            status.put("lastCheckInStatus", lastCheckIn.getStatus());

            long hoursSinceLastCheck = ChronoUnit.HOURS.between(
                    lastCheckIn.getCheckInTime(),
                    LocalDateTime.now()
            );
            status.put("hoursSinceLastCheck", hoursSinceLastCheck);

            String currentStatus;
            if (hoursSinceLastCheck <= ward.getCheckIntervalHours()) {
                currentStatus = "OK";
            } else if (hoursSinceLastCheck <= ward.getCheckIntervalHours() * 2) {
                currentStatus = "WARNING";
            } else {
                currentStatus = "DANGER";
            }
            status.put("currentStatus", currentStatus);

        } catch (RuntimeException e) {
            status.put("lastCheckIn", null);
            status.put("currentStatus", "NO_CHECKINS");
            status.put("hoursSinceLastCheck", null);
        }

        return status;
    }

    @Scheduled(fixedRate = 300000)
    public void checkAllWards() {
        System.out.println("=== Checking all wards for missed check-ins ===");
        System.out.println("Time: " + LocalDateTime.now());

        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if ("WARD".equals(user.getUserType())) {
                Ward ward = (Ward) user;

                if (ward.getNotificationsEnabled() != null && ward.getNotificationsEnabled()) {
                    checkWardStatus(ward);
                }
            }
        }
    }

    private void checkWardStatus(Ward ward) {
        try {
            CheckIn lastCheckIn = checkInRepository
                    .findFirstByWardOrderByCheckInTimeDesc(ward)
                    .orElse(null);

            if (lastCheckIn == null) {
                System.out.println("Ward " + ward.getName() + " has no check-ins");
                return;
            }

            long hoursSinceLastCheck = ChronoUnit.HOURS.between(
                    lastCheckIn.getCheckInTime(),
                    LocalDateTime.now()
            );

            System.out.println("Ward: " + ward.getName() +
                    ", Last check: " + lastCheckIn.getCheckInTime() +
                    ", Hours ago: " + hoursSinceLastCheck +
                    ", Interval: " + ward.getCheckIntervalHours());

            if (hoursSinceLastCheck > ward.getCheckIntervalHours()) {
                System.out.println("⚠️ ALERT: Ward " + ward.getName() +
                        " missed check-in! " + hoursSinceLastCheck + " hours passed.");

                notificationService.sendMissedCheckInNotification(ward, hoursSinceLastCheck);
            }

        } catch (Exception e) {
            System.err.println("Error checking ward " + ward.getName() + ": " + e.getMessage());
        }
    }

    public Map<String, Object> getStatistics(Long wardId) {
        User ward = userRepository.findById(wardId)
                .orElseThrow(() -> new RuntimeException("Ward not found"));

        if (!"WARD".equals(ward.getUserType())) {
            throw new RuntimeException("User is not a Ward");
        }

        Map<String, Object> stats = new HashMap<>();
        List<CheckIn> allCheckIns = checkInRepository.findByWardOrderByCheckInTimeDesc(ward);

        stats.put("totalCheckIns", allCheckIns.size());
        stats.put("wardName", ward.getName());

        if (!allCheckIns.isEmpty()) {
            CheckIn lastCheckIn = allCheckIns.get(0);
            stats.put("lastCheckInTime", lastCheckIn.getCheckInTime());
            stats.put("lastCheckInStatus", lastCheckIn.getStatus());

            CheckIn firstCheckIn = allCheckIns.get(allCheckIns.size() - 1);
            stats.put("firstCheckInTime", firstCheckIn.getCheckInTime());

            if (allCheckIns.size() > 1) {
                long totalHours = 0;
                for (int i = 0; i < allCheckIns.size() - 1; i++) {
                    long hoursBetween = ChronoUnit.HOURS.between(
                            allCheckIns.get(i + 1).getCheckInTime(),
                            allCheckIns.get(i).getCheckInTime()
                    );
                    totalHours += hoursBetween;
                }
                double avgHours = (double) totalHours / (allCheckIns.size() - 1);
                stats.put("averageHoursBetweenCheckIns", Math.round(avgHours * 10) / 10.0);
            }
        }

        LocalDateTime weekAgo = LocalDateTime.now().minusDays(7);
        long checkInsLastWeek = allCheckIns.stream()
                .filter(checkIn -> checkIn.getCheckInTime().isAfter(weekAgo))
                .count();
        stats.put("checkInsLast7Days", checkInsLastWeek);

        return stats;
    }
}