package com.cyj.service.adminService.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.dto.AdminLoginDTO;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;
import com.cyj.entity.Admin;
import com.cyj.mapper.AdminMapper;
import com.cyj.mapper.UserMapper;
import com.cyj.service.adminService.AdminService;
import com.cyj.service.userService.UserService;
import com.cyj.utils.JwtUtil;
import com.cyj.utils.RedissonUtil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("adminLoginServiceImpl")
@Slf4j
public class LoginServiceImpl implements AdminService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Result<AccessTokenDVO> adminLogin(AdminLoginDTO adminLoginDTO) {

        String url = request.getRequestURI();

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(adminLoginDTO.getUsername(), adminLoginDTO.getPassword()));
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("登陆认证失败");
        }
        List<String> roles = new ArrayList<>();

        //获取权限列表
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(grantedAuthority -> roles.add(grantedAuthority.getAuthority()));
        }

        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", adminLoginDTO.getUsername());
        Admin admin = adminMapper.selectOne(queryWrapper);

        //生成访问令牌
        Map<String,Object> accessClaims = new HashMap<>();
        accessClaims.put("id",admin.getAdminId().toString());
        accessClaims.put("roles", roles);
        String accessToken = jwtUtil.generateAccessToken(accessClaims);


        //生成刷新令牌
        Map<String,Object> refreshClaims = new HashMap<>();
        refreshClaims.put("id",admin.getAdminId().toString());
        refreshClaims.put("roles", roles);
        String refreshToken = jwtUtil.generaterefreshToken(refreshClaims);

        //将刷新令牌存入缓存
        String key = "toke" + ":admin:" + adminLoginDTO.getUsername();
        redissonUtil.setValue(key, refreshToken,7,TimeUnit.DAYS);

        AccessTokenDVO accessTokenDVO = AccessTokenDVO.builder()
                .access_token(accessToken)
                .refresh_token_key(key)
                .build();

        //返回访问令牌
        return Result.success(accessTokenDVO);
    }
}
