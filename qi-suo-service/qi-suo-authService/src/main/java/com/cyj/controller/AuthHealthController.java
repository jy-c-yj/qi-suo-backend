package com.cyj.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthHealthController {
    @RequestMapping("/api/auth/health")
    public String health() {
        return "authService is ok";
    }
}
