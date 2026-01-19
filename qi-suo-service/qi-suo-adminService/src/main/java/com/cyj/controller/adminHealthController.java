package com.cyj.controller;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class adminHealthController {
    @RequestMapping("/health")
//    @PreAuthorize("hasAnyRole('user')")
    public String health() {
        return "adminService is ok";
    }
}
