package com.cyj.config;



import com.cyj.common.WhitelistURL;
import com.cyj.filter.authFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;


import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class securityConfig {

    @Autowired
    private authFilter authFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        // 创建基于内存的用户信息管理器
//        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
//        manager.createUser(
//                // 创建UserDetails对象，用于管理用户名、用户密码、用户角色、用户权限等内容
//                User.builder().username("admin").password(passwordEncoder().encode("admin")).roles("admin").build()
//        );
//        return manager;
//    }

    // 定义CORS配置源
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        //config.setAllowedOrigins(List.of("*")); // 允许的源列表
        config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS")); // 允许的方法
        config.setAllowedHeaders(List.of("*")); // 允许的头
        config.addAllowedOriginPattern("*");  //
        config.setAllowCredentials(true); // 允许凭据
        config.setMaxAge(3600L); // 预检缓存时间
        // 为所有路径应用此配置
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable) // 基于token，不需要csrf
//                .formLogin(Customizer.withDefaults())
                .sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 基于token，不需要session
                .authorizeHttpRequests((authz) -> authz
                        .requestMatchers(
                                "/health",
                                "/*/*/*/login/**",
                                "/api/auth/kaptcha/**",
                                "/api/*/test/**",
                                "/api/admin/image/getCarouselImageList",
                                "/api/admin/image/getNowHomeBackGroundImage",
                                WhitelistURL.Refresh_Token_Url,
                                WhitelistURL.File_Upload_Url,
                                WhitelistURL.User_Register_Url,
                                WhitelistURL.home_getBackGroundImage_Url
                                ).permitAll() // 放行api
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated()
                );
        httpSecurity.addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
        //httpSecurity.cors(config -> config.configurationSource(corsConfigurationSource()));
        return httpSecurity.build();
    }
}
