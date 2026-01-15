package com.cyj;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

@Component
@Slf4j
@Data
@ConfigurationProperties(prefix = "jwt")
public class asdJwtUtil {

    private String secretKey = "Y6mQMQ+jDKiAp1jwzAqLa7fNp0GzMx54QYpqDIpdZe4=";
    //刷新令牌过期时间 7d
    private String refreshExpire = "86400";
    //访问令牌过期时间 24h
    private String accessExpire = "604800";

    private SecretKey key;

    @PostConstruct
    private void generateSecretKey() {
        this.key =new SecretKeySpec(Base64.getDecoder().decode(secretKey),"HmacSHA256");
        log.info("jwt安全密钥初始化成功");
    }

    /**
     * 生成访问令牌
     * @param claims
     * @return
     */
    public String generateAccessToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(accessExpire) * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
    /**
     * 生成刷新令牌
     * @param claims
     * @return
     */
    public String generaterefreshToken(Map<String, Object> claims) {
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(refreshExpire) * 1000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims parseToken(String token) {

        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();

    }

}
