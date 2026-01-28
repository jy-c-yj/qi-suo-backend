package com.cyj.filter;


import com.alibaba.fastjson.JSON;
import com.cyj.common.JwtClaimType;

import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Component
@Slf4j
public class authFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request, jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain) throws ServletException, IOException, ServletException {
        String json = request.getHeader("Auth-claims");
        if(json == null) {
            log.error("获取权限出错");
            filterChain.doFilter(request, response);
            return;
        }
        JwtClaimType jwtClaimType = JSON.parseObject(json, JwtClaimType.class);
        //获取权限
        Collection<? extends GrantedAuthority> collection = jwtClaimType.getRoles().stream().map(SimpleGrantedAuthority::new).toList();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(jwtClaimType.getId(),null,collection));

        log.info("用户 Id：{} , Auth：{}", jwtClaimType.getId(), jwtClaimType.getRoles());

        filterChain.doFilter(request, response);
    }
}
