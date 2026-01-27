package com.cyj.filter;

import com.alibaba.fastjson.JSON;
import com.cyj.common.JwtClaimType;
import com.cyj.common.Result;
import com.cyj.common.WhitelistURL;
import com.cyj.utils.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.List;


@Component
@Slf4j
public class JwtFilter implements WebFilter {
    @Autowired
    private JwtUtil jwtUtil;
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        //获取token
        HttpHeaders headers = exchange.getRequest().getHeaders();
        String token = headers.getFirst("Authorization-token");
        String url = exchange.getRequest().getURI().getPath();

        if(token == null || WhitelistURL.Refresh_Token_Url.equals(url)) {
            //登陆操作，放行
            return chain.filter(exchange);
        }

        Claims claims = null;
        //校验token
        try {
             claims = jwtUtil.parseToken(token);
        }catch (ExpiredJwtException  e) {
            log.error("访问令牌过期");
            return returnErrorResponse(exchange,"访问令牌过期");
        }
        //获取用户/商家/信息
        JwtClaimType jwtClaimType = new JwtClaimType();

        Long id = Long.parseLong(claims.get("id").toString());
        jwtClaimType.setId(id);
        jwtClaimType.setRoles((List<String>) claims.get("roles"));

        String json = JSON.toJSONString(jwtClaimType);

        //存入请求头
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.putAll(exchange.getRequest().getHeaders());
        httpHeaders.set("Auth-claims", json);

        ServerHttpRequest serverHttpRequest = exchange.getRequest().mutate()
                .headers(httpHeaders1 -> httpHeaders1.putAll(httpHeaders))
                .build();

        return chain.filter(exchange.mutate().request(serverHttpRequest).build());
    }
    /**
     * 返回错误响应
     */
    private Mono<Void> returnErrorResponse(ServerWebExchange exchange, String errorMsg) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json;charset=UTF-8");
        // 构建Result错误对象
        Result<Object> result = Result.error(errorMsg);
        String responseBody = JSON.toJSONString(result);
        DataBuffer buffer = exchange.getResponse().bufferFactory()
                .wrap(responseBody.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(buffer));

    }
}
