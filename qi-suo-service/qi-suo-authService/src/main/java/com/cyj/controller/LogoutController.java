package com.cyj.controller;

import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.service.LogoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class LogoutController {

    @Autowired
    private LogoutService logoutService;

    /**
     * 退出登录：删除 Redis 刷新令牌，access token 自然过期
     */
    @Log(module = "认证", desc = "退出登录")
    @PreAuthorize("isAuthenticated()")
    @PostMapping("/logout")
    public Result<String> logout() {
        return logoutService.logout();
    }
}
