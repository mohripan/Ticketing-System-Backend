package com.example.TicketingSystemBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redisTemplate;

    public boolean exists(String key) {
        return redisTemplate.hasKey(key);
    }
}
