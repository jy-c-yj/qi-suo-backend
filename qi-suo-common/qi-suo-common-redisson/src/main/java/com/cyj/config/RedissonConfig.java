package com.cyj.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedissonConfig {

    @Value("${spring.data.redis.host:127.0.0.1}")
    private String redisHost;
    @Value("${spring.data.redis.port:6379}")
    private int redisPort;
    @Value("${spring.data.redis.password:}")
    private String redisPassword;
    @Value("${spring.data.redis.database:0}")
    private int redisDatabase;
    @Bean
    public RedissonClient RedissonClient() {
        Config config = new Config();
        SingleServerConfig server = config.useSingleServer().setAddress(("redis://" + redisHost + ":" + redisPort))
                .setDatabase(redisDatabase);
        // 注意：@Value("${...:}") 的默认值是 "" 而不是 null。只判 null 的话，
        // 没配密码也会执行 setPassword("") → Redisson 发 AUTH → Redis 报
        // "ERR AUTH <password> called without any password configured"。
        if (redisPassword != null && !redisPassword.isBlank()) {
            server.setPassword(redisPassword);
        }

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        config.setCodec(new JsonJacksonCodec(objectMapper));

        return Redisson.create(config);

    }
}
