package com.example.TicketingSystemBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveToken(String email, String token) {
        redisTemplate.opsForValue().set(email, token);
        //redisTemplate.expire(email, jwtExpirationTime, TimeUnit.SECONDS);
    }

    public String getToken(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }
}
