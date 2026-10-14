package com.cyj;



import com.cyj.utils.RedissonUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;
import java.util.concurrent.TimeUnit;


@SpringBootTest
public class TestApplication {
    @Autowired
    HttpServletRequest request;
    @Autowired
    RedissonClient redissonClient;
    @Autowired
    AsdJwtUtil asdJwtUtil;
    @Test
    public void test() {
        System.out.println(Calendar.getInstance().getTime());
    }
    @Test
    public void contextLoads() {
        Map<String, Object> map = new HashMap<>();
        map.put("a", "1");
        map.put("b", "2");
        map.put("c", "3");
        String s = asdJwtUtil.generaterefreshToken(map);
        System.out.println(s);
        Claims claims = asdJwtUtil.parseToken(s);
        System.out.println(claims.get("a"));

    }
    @Test
    public void RedissonTest() {
        String url = request.getRequestURL().toString();
        System.out.println(url);

    }


}
