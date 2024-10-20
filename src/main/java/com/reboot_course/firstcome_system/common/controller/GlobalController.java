package com.reboot_course.firstcome_system.common.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GlobalController {

    @GetMapping("/api/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("health check good");
    }
}