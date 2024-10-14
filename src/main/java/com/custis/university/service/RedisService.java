package com.custis.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public <T> void cacheValue(String key, T value) {
        try {
            redisTemplate.opsForValue().set(key, value);
        } catch (Exception e) {
            logger.error("Error caching value: {}", e.getMessage(), e);
        }
    }

    public <T> T getCachedValue(String key, Class<T> type) {
        try {
            Object value = redisTemplate.opsForValue().get(key);
            return type.cast(value);
        } catch (Exception e) {
            //написать отдельный обработчик исключений
            logger.error("Error retrieving cached value: {}", e.getMessage(), e);
            return null;
        }
    }

    public void deleteCacheValue(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            logger.error("Error deleting cached value: {}", e.getMessage(), e);
        }
    }

    public void cacheValueWithExpiration(String key, Object value, long timeout) {
        try {
            redisTemplate.opsForValue().set(key, value, timeout);
        } catch (Exception e) {
            logger.error("Error caching value with expiration: {}", e.getMessage(), e);
        }
    }
}
