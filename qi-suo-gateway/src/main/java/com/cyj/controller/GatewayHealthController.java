package com.cyj.controller;


import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
public class GatewayHealthController {
    @RequestMapping("/health")
    public String health() {
        return "gatewayService is ok";
    }
}
