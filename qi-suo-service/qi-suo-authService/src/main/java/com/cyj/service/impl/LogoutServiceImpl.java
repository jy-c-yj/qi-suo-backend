package com.cyj.service.impl;

import com.cyj.common.Result;
import com.cyj.constants.RoleConstants;
import com.cyj.entity.Admin;
import com.cyj.entity.Merchant;
import com.cyj.entity.User;
import com.cyj.mapper.AdminMapper;
import com.cyj.mapper.MerchantMapper;
import com.cyj.mapper.UserMapper;
import com.cyj.service.LogoutService;
import com.cyj.utils.RedissonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class LogoutServiceImpl implements LogoutService {

    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private MerchantMapper merchantMapper;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Result<String> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof Number principal)) {
            // 未携带有效身份时按幂等处理，前端仍会清理本地会话
            return Result.success();
        }
        Long id = principal.longValue();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        // 服务间调用没有刷新令牌
        if (roles.contains(RoleConstants.SYSTEM)) {
            return Result.success();
        }

        String refreshKey = null;
        try {
            if (roles.contains(RoleConstants.MERCHANT)) {
                Merchant merchant = merchantMapper.selectById(id);
                if (merchant != null) {
                    refreshKey = "toke:merchant:" + merchant.getMerchantName();
                }
            } else if (roles.contains(RoleConstants.USER)) {
                User user = userMapper.selectById(id);
                if (user != null) {
                    refreshKey = "toke:user:" + user.getUsername();
                }
            } else if (roles.contains(RoleConstants.SystemADMIN)
                    || roles.contains(RoleConstants.SuperADMIN)
                    || roles.contains(RoleConstants.OrdinaryADMIN)) {
                Admin admin = adminMapper.selectById(id);
                if (admin != null) {
                    refreshKey = "toke:admin:" + admin.getUsername();
                }
            }
        } catch (Exception e) {
            log.error("退出登录查询账号信息失败, id={}", id, e);
        }

        if (refreshKey != null) {
            redissonUtil.delete(refreshKey);
        }
        return Result.success();
    }
}
