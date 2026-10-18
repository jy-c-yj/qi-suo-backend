package com.cyj.service;

import com.cyj.common.Result;

/**
 * 退出登录：删除 Redis 中对应的刷新令牌
 */
public interface LogoutService {
    Result<String> logout();
}
