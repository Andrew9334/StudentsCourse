package com.custus.university.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisService.class);
    private final ReactiveRedisTemplate<String, Object> reactiveRedisTemplate;

    public RedisService(ReactiveRedisTemplate<String, Object> redisTemplate) {
        this.reactiveRedisTemplate = redisTemplate;
    }

    public <T> Mono<Void> cacheValue(String key, T value) {
        return reactiveRedisTemplate.opsForValue()
                .set(key, value)
                .doOnSuccess(result -> logger.info("Value cached with key: {}", key))
                .doOnError(e -> logger.error("Error caching value for key {}: {}", key, e.getMessage(), e))
                .then();
    }

    public <T> Mono<T> getCachedValue(String key, Class<T> type) {
        return reactiveRedisTemplate.opsForValue()
                .get(key)
                .map(value -> type.cast(value))
                .doOnSuccess(result -> {
                    if (result == null) {
                        logger.warn("No value found for key: {}", key);
                    }
                })
                .doOnError(e -> logger.error("Error retrieving cached value for key {}: {}", key, e.getMessage(), e));
    }

    public Mono<Void> deleteCacheValue(String key) {
        return reactiveRedisTemplate.delete(key)
                .doOnSuccess(aVoid -> logger.info("Cache entry deleted for key: {}", key))
                .doOnError(e -> logger.error("Error deleting cached value for key {}: {}", key, e.getMessage(), e))
                .then();
    }

    public <T> Mono<Void> cacheValueWithExpiration(String key, Object value, long timeout) {
        return reactiveRedisTemplate.opsForValue()
                .set(key, value, Duration.ofSeconds(timeout))
                .doOnSuccess(aVoid -> logger.info("Value cached with expiration for key: {}", key))
                .doOnError(e -> logger.error("Error caching value with expiration for key {}: {}", key, e.getMessage(), e))
                .then();
    }
}
