package com.cyj.service.merchantService.Impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.cyj.common.Result;
import com.cyj.dto.MerchantLoginDTO;
import com.cyj.dvo.AccessTokenDVO;
import com.cyj.entity.Merchant;
import com.cyj.mapper.MerchantMapper;
import com.cyj.service.merchantService.MerchantService;
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

@Service("merchantLoginServiceImpl")
@Slf4j
public class LoginServiceImpl implements MerchantService {
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private RedissonUtil redissonUtil;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public Result<AccessTokenDVO> merchantLogin(MerchantLoginDTO merchantLoginDTO) {



        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(merchantLoginDTO.getMerchantName(), merchantLoginDTO.getMerchantPassword()));
        if (authentication == null || !authentication.isAuthenticated()) {
            log.error("登陆认证失败");
        }
        List<String> roles = new ArrayList<>();

        //获取权限列表
        if (authentication != null) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            authorities.forEach(grantedAuthority -> roles.add(grantedAuthority.getAuthority()));
        }
        QueryWrapper<Merchant> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("Merchant_name", merchantLoginDTO.getMerchantName());
        Merchant merchant = merchantMapper.selectOne(queryWrapper);

        //生成访问令牌
        Map<String,Object> accessClaims = new HashMap<>();
        accessClaims.put("id",merchant.getMerchantId().toString());
        accessClaims.put("roles", roles);
        String accessToken = jwtUtil.generateAccessToken(accessClaims);

        //生成刷新令牌
        Map<String,Object> refreshClaims = new HashMap<>();
        refreshClaims.put("id",merchant.getMerchantId().toString());
        refreshClaims.put("roles", roles);
        String refreshToken = jwtUtil.generaterefreshToken(refreshClaims);

        //将刷新令牌存入缓存
        String key = "toke" + ":merchant:" + merchant.getMerchantName();
        redissonUtil.setValue(key, refreshToken,7,TimeUnit.DAYS);

        AccessTokenDVO accessTokenDVO = AccessTokenDVO.builder()
                .access_token(accessToken)
                .refresh_token_key(key)
                .build();

        //返回访问令牌
        return Result.success(accessTokenDVO);
    }
}
