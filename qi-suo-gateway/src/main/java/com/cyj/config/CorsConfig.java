package com.cyj.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
public class CorsConfig {

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");

        // 允许的请求源（注意：使用 addAllowedOrigin 或 addAllowedOriginPattern）
        // 如果使用通配符 *，需要同时设置 setAllowCredentials(false)
        // 如果需要携带凭证（cookies等），不能使用通配符 *
        corsConfiguration.addAllowedOriginPattern("*");

        // 允许的请求方法
        corsConfiguration.setAllowedMethods(Arrays.asList(
                HttpMethod.GET.name(),
                HttpMethod.POST.name(),
                HttpMethod.PUT.name(),
                HttpMethod.DELETE.name(),
                HttpMethod.OPTIONS.name(),
                HttpMethod.PATCH.name(),
                HttpMethod.HEAD.name()
        ));

        // 是否允许凭证（cookies、认证信息等）
        corsConfiguration.setAllowCredentials(true);

        // 预检请求的缓存时间（单位：秒）
        corsConfiguration.setMaxAge(3600L);

        // 暴露的响应头
        corsConfiguration.addExposedHeader("Authorization");
        corsConfiguration.addExposedHeader("X-Total-Count");
        corsConfiguration.addExposedHeader("Content-Disposition");

        // 创建响应式的 CORS 配置源
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);

        return new CorsWebFilter(source);
    }
}