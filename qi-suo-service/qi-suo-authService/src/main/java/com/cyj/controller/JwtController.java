package com.cyj.controller;


import com.cyj.Annotation.Log;
import com.cyj.common.Result;
import com.cyj.utils.JwtUtil;
import com.cyj.utils.RedissonUtil;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth/jwt")
public class JwtController {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedissonUtil redissonUtil;
    @Log(module = "认证", desc = "刷新令牌")
    @GetMapping("/refresh")
    public Result<String> refresh(String refresh_token_key) {
        //从redis查询刷新令牌
        String refreshToken = redissonUtil.getValue(refresh_token_key);

        if(refreshToken == null) {
            //登陆已过期请重新登陆
            return Result.error("登陆已过期请重新登陆");
        }

        Claims claims = jwtUtil.parseToken(refreshToken);
        //生成新的访问令牌
        String accessToken = jwtUtil.generateAccessToken(claims);


        //返回令牌
        return Result.success(accessToken);
    }
}
