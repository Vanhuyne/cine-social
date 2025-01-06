package com.huy.backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/auth")
public class AuthController {
    @GetMapping("/")
    public String home() {
        return "Hello, public user!";
    }

    @GetMapping("/secure")
    public String secured() {
        return "Hello, logged in user!";
    }
}
