package com.cyj.controller;



import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserHealthController {
    @RequestMapping("/health")
    public String health() {
        return "userService is ok";
    }
}
