package com.cyj.config;

import org.springframework.beans.factory.annotation.Value;
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

    /**
     * 允许的前端来源，多个用逗号分隔，支持 Spring 的 origin pattern 语法。
     * 默认覆盖本机任意端口：Vite dev 5173、Nginx dev 8088 都能直接使用。
     * 生产环境在 Nacos 的 qi-suo-gateway.yaml 里配置真实域名，例如：
     *   cors:
     *     allowed-origins: https://qi-suo.example.com
     */
    @Value("${cors.allowed-origins:http://localhost:[*],http://127.0.0.1:[*]}")
    private String allowedOrigins;

    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        // 允许的请求头
        corsConfiguration.addAllowedHeader("*");

        // 允许的请求源。用 pattern 而不是固定端口，避免每换一个前端端口就 403：
        // 浏览器即使同源，POST 也会带 Origin，网关的 CorsWebFilter 会校验它
        for (String origin : allowedOrigins.split(",")) {
            String pattern = origin.trim();
            if (!pattern.isEmpty()) {
                corsConfiguration.addAllowedOriginPattern(pattern);
            }
        }

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