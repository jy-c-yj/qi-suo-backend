package com.interceptors;


import com.alibaba.fastjson.JSON;
import com.cyj.common.JwtClaimType;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class AuthFeignClientInterceptor implements RequestInterceptor {
    @Override
    public void apply(RequestTemplate template) {
        log.info("FileFeignClientInterceptor 请求拦截器启动");
        JwtClaimType jwtClaimType = new JwtClaimType();

        jwtClaimType.setId(Long.parseLong("-1"));
        jwtClaimType.setRoles(List.of("system"));

        String json = JSON.toJSONString(jwtClaimType);

        //设置请求头
        template.header("Auth-claims", json);

    }
}
