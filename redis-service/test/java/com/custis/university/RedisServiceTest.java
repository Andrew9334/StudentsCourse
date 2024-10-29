package com.custis.university;

import com.custis.university.model.Course;
import com.custus.university.service.RedisService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;

public class RedisServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @InjectMocks
    private RedisService redisService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCacheValue() {
        String key = "testKey";
        String value = "testValue";

        redisService.cacheValue(key, value);
        verify(redisTemplate, times(1))
                .opsForValue()
                .set(key, value);
    }

    @Test
    public void testGetCachedValue() {
        String key = "testKey";
        String value = "testValue";

        when(redisTemplate.opsForValue().get(key)).thenReturn(value);

        String cachedValue = String.valueOf(redisService.getCachedValue(key, Course.class));
        assertEquals(value, cachedValue);
    }

    @Test
    public void testGetCachedValueNonExistingKey() {
        String key = "nonExistingKey";

        when(redisTemplate.opsForValue().get(key)).thenReturn(null);

        String cachedValue = String.valueOf(redisService.getCachedValue(key, Course.class));
        assertNull(cachedValue);
    }

    @Test
    public void testDeletedCacheValue() {
        String key = "testKey";

        redisService.deleteCacheValue(key);

        verify(redisTemplate, times(1)).delete(key);
    }

    @Test
    public void testCacheValueWithExpiration() {
        String key = "testKey";
        String value = "testValue";
        long timeout = 60;

        redisService.cacheValueWithExpiration(key, value, timeout);
        verify(redisTemplate, times(1)).opsForValue().set(key, value, timeout);
    }
}
