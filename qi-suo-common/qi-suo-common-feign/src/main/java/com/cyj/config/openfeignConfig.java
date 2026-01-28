package com.cyj.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class openfeignConfig {
    @Bean
    Logger.Level feignLoggerLevel(){//日志
        return Logger.Level.FULL;
    }
}