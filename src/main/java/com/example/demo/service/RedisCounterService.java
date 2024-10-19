package com.example.demo.service;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RedisCounterService {

    @Autowired
    private StringRedisTemplate redisTemplate;

    public void incrementSuccess() {
        redisTemplate.opsForValue().increment("SUCCESS");
    }

    public void incrementFailure() {
        redisTemplate.opsForValue().increment("FAILURE");
    }

    public Long getSuccessCount() {
        String value = redisTemplate.opsForValue().get("SUCCESS");
        return value != null ? Long.parseLong(value) : 0L;
    }

    public Long getFailureCount() {
        String value = redisTemplate.opsForValue().get("FAILURE");
        return value != null ? Long.parseLong(value) : 0L;
    }
}
