package com.carecheck.service;

import com.carecheck.entity.Relative;
import com.carecheck.entity.User;
import com.carecheck.entity.Ward;
import com.carecheck.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;

@Service
public class NotificationService {

    @Autowired
    private UserRepository userRepository;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Отправить уведомление о пропущенной отметке всем родственникам подопечного
     */
    public void sendMissedCheckInNotification(Ward ward, long hoursSinceLastCheck) {
        System.out.println("=".repeat(60));
        System.out.println("📢 CRITICAL NOTIFICATION: MISSED CHECK-IN");
        System.out.println("=".repeat(60));

        // Получаем всех родственников этого подопечного
        List<User> allUsers = userRepository.findAll();

        for (User user : allUsers) {
            if ("RELATIVE".equals(user.getUserType())) {
                Relative relative = (Relative) user;

                // Проверяем, следит ли этот родственник за данным подопечным
                if (isRelativeWatchingWard(relative, ward)) {
                    sendNotificationToRelative(relative, ward, hoursSinceLastCheck);
                }
            }
        }

        System.out.println("=".repeat(60));
    }

    /**
     * Проверяет, следит ли родственник за данным подопечным
     */
    private boolean isRelativeWatchingWard(Relative relative, Ward ward) {
        // Теперь проверяем реальные связи
        return relative.getWards().contains(ward);
    }

    /**
     * Отправить уведомление конкретному родственнику
     */
    private void sendNotificationToRelative(Relative relative, Ward ward, long hoursSinceLastCheck) {
        System.out.println("👤 To: " + relative.getName() + " (" + relative.getEmail() + ")");
        System.out.println("   📍 Ward: " + ward.getName() + " (ID: " + ward.getId() + ")");
        System.out.println("   ⏰ Last check-in: " +
                (ward.getLastCheckIn() != null ?
                        ward.getLastCheckIn().format(formatter) : "NEVER"));
        System.out.println("   🚨 Hours passed: " + hoursSinceLastCheck + " (expected: " +
                ward.getCheckIntervalHours() + " hours)");
        System.out.println("   📞 Ward phone: " + ward.getPhoneNumber());

        if (ward.getAddress() != null && !ward.getAddress().isEmpty()) {
            System.out.println("   🏠 Address: " + ward.getAddress());
        }

        System.out.println("   ---");

        // Здесь будет реальная отправка:
        // 1. Email (если receiveEmailNotifications = true)
        // 2. SMS (если receiveSmsNotifications = true)
        // 3. Push уведомление
    }

    /**
     * Отправить уведомление о новой отметке
     */
    public void sendCheckInNotification(Ward ward) {
        System.out.println("✅ POSITIVE NOTIFICATION: CHECK-IN RECEIVED");
        System.out.println("   Ward: " + ward.getName());
        System.out.println("   Time: " + LocalDateTime.now().format(formatter));

        // В будущем: уведомление родственникам, что всё в порядке
    }

    /**
     * Тестовый метод для ручной отправки уведомлений
     */
    public void sendTestNotification() {
        System.out.println("🔔 TEST NOTIFICATION SYSTEM");

        // Находим тестового подопечного
        User wardUser = userRepository.findByUsername("grandma_anna")
                .orElseThrow(() -> new RuntimeException("Test ward not found"));

        if ("WARD".equals(wardUser.getUserType())) {
            Ward ward = (Ward) wardUser;
            sendMissedCheckInNotification(ward, 30); // 30 часов прошло
        }
    }
}