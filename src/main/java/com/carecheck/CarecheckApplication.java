package com.carecheck;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Добавьте эту аннотацию!
public class CarecheckApplication {
    public static void main(String[] args) {
        SpringApplication.run(CarecheckApplication.class, args);
    }
}