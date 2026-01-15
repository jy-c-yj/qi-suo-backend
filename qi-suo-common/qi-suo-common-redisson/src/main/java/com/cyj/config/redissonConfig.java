package com.cyj.config;


import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class redissonConfig {
    @Bean
    public RedissonClient RedissonClient() {
        Config config = new Config();
        config.useSingleServer().setAddress("redis://127.0.0.1:6379");
        Codec codec = new JsonJacksonCodec();
        config.setCodec(codec);
        return Redisson.create(config);

    }
}
