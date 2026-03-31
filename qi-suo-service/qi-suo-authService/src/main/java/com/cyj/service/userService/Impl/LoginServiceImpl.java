package com.cyj.service.userService.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.constant.RedissonKey;
import com.cyj.dto.UserLoginDTO;
import com.cyj.dvo.AccessTokenDVO;
import com.cyj.dvo.UserInformationDVO;
import com.cyj.entity.User;
import com.cyj.mapper.UserMapper;
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

import java.util.*;
import java.util.concurrent.TimeUnit;

@Service("userLoginServiceImpl")
@Slf4j
public class LoginServiceImpl implements UserService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedissonUtil redissonUtil;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserMapper userMapper;
    @Override
    public Result<AccessTokenDVO> userLogin(UserLoginDTO userLoginDTO) {


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
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", userLoginDTO.getUsername());
        User user = userMapper.selectOne(queryWrapper);

        //生成访问令牌
        Map<String,Object> accessclaims = new HashMap<>();
        accessclaims.put("id",user.getId().toString());
        accessclaims.put("roles", roles);
        String accessToken = jwtUtil.generateAccessToken(accessclaims);
        //生成刷新令牌
        Map<String,Object> refreshclaims = new HashMap<>();
        refreshclaims.put("id",user.getId().toString());
        refreshclaims.put("roles", roles);
        String refreshToken = jwtUtil.generaterefreshToken(refreshclaims);

        //将刷新令牌存入缓存
        String key = "toke" + ":user:" + userLoginDTO.getUsername();
        redissonUtil.setValue(key, refreshToken,7,TimeUnit.DAYS);

        AccessTokenDVO accessTokenDVO = AccessTokenDVO.builder()
                .access_token(accessToken)
                .refresh_token_key(key)
                .build();

        //将用户信息存入缓存
        UserInformationDVO userInformationDVO = UserInformationDVO.toUserInformationDVO(user);
        String userKey = RedissonKey.INFORMATION_USER_KEY + user.getId();
        redissonUtil.setValue(userKey, userInformationDVO,7,TimeUnit.DAYS);
        //返回访问令牌
        return Result.success(accessTokenDVO);
    }
}
