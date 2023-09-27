package com.example.TicketingSystemBackend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class TokenService {
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Value("${jwt.expiration.time}")
    private long jwtExpirationTime;

    public void saveToken(String email, String token) {
        redisTemplate.opsForValue().set(email, token, jwtExpirationTime, TimeUnit.MILLISECONDS);
        //redisTemplate.expire(email, jwtExpirationTime, TimeUnit.SECONDS);
    }

    public void invalidateToken(String email) {
        redisTemplate.delete(email);
    }

    public String getToken(String email) {
        return (String) redisTemplate.opsForValue().get(email);
    }
}
