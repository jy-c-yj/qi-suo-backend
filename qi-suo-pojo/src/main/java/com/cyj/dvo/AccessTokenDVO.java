package com.cyj.dvo;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class AccessTokenDVO {
    private String access_token;
    private String refresh_token_key;
    /**
     * 登录主体 id（用户 id / 商家 id / 管理员 id）
     */
    private Long id;
    /**
     * 权限列表（如 USER、Merchant、SystemADMIN）
     */
    private List<String> roles;
}
