package com.cyj.service.Impl;


import com.cyj.common.Result;
import com.cyj.dto.UserLoginDTO;
import com.cyj.mapper.UserMapper;
import com.cyj.service.UserService;
import com.cyj.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result<String> userLogin(UserLoginDTO userLoginDTO) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(userLoginDTO.getUsername(), userLoginDTO.getPassword()));
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("登陆认证失败");
        }
        List<String> roles = new ArrayList<>();

        //获取权限列表
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(grantedAuthority -> roles.add(grantedAuthority.getAuthority()));
        }

        //生成访问令牌
        Map<String,Object> accessclaims = new HashMap<>();
        accessclaims.put("username",userLoginDTO.getUsername());
        accessclaims.put("roles", roles);
        String accessToken = jwtUtil.generateAccessToken(accessclaims);
        //生成刷新令牌
        Map<String,Object> refreshclaims = new HashMap<>();
        refreshclaims.put("username",userLoginDTO.getUsername());
        String refreshToken = jwtUtil.generaterefreshToken(refreshclaims);
        //将刷新令牌存入缓存




        return Result.success(accessToken);
    }
}
