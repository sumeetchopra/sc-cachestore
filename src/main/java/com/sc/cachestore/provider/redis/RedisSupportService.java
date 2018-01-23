package com.sc.cachestore.provider.redis;

import com.sc.cachestore.provider.cachestore.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;

@Component
public class RedisSupportService implements CacheService {
    private static final String REDIS = "Redis";

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String> hashStringOperations;

    @Resource(name = "stringRedisTemplate")
    private HashOperations<String, String, String[]> hashStringArrayOperations;

    @Autowired
    private RedisTemplate<String, Long> longRedisTemplate;

    @Resource(name = "stringRedisTemplate")
    private SetOperations<String, String> setStringOperations;

    @Resource(name = "stringRedisTemplate")
    private ZSetOperations<String, String> sortedSetStringOperations;

    @Override
    public void hashMapPut(String keySpace, String key, String value) {
        hashMapPut(hashStringOperations, keySpace, key, value);
    }

    private <T> void hashMapPut(HashOperations<String, String, T> hashOperations, String keySpace, String key,
                                T value) {
        hashOperations.put(keySpace, key, value);
    }

    @Override
    public String hashMapGet(String keySpace, String key) {
        return hashMapGet(hashStringOperations, keySpace, key);
    }

    @Override
    public Map<String, String> hashMapEntries(String keySpace) {
        return hashStringOperations.entries(keySpace);
    }

    @Override
    public Set<String> hashMapKeys(String keySpace) {
        return hashMapKeys(hashStringOperations, keySpace);
    }

    private <T> T hashMapGet(HashOperations<String, String, T> hashOperations, String keySpace, String key) {
        return hashOperations.get(keySpace, key);
    }

    private <T> Set<String> hashMapKeys(HashOperations<String, String, T> hashOperations, String keySpace) {
        return hashOperations.keys(keySpace);
    }

    @Override
    public void setAtomicLongValue(String keySpace, Long value) {
        RedisAtomicLong counter = new RedisAtomicLong(keySpace, longRedisTemplate.getConnectionFactory());
        counter.set(value);
    }

    @Override
    public Long getAtomicLongValue(String keySpace) {
        RedisAtomicLong counter = new RedisAtomicLong(keySpace, longRedisTemplate.getConnectionFactory());
        return counter.get();
    }

    @Override
    public Long incrementAtomicLongValue(String keySpace, Long value) {
        RedisAtomicLong counter = new RedisAtomicLong(keySpace, longRedisTemplate.getConnectionFactory());
        return counter.addAndGet(value);
    }

    @Override
    public Long decrementAtomicLongValue(String keySpace) {
        RedisAtomicLong counter = new RedisAtomicLong(keySpace, longRedisTemplate.getConnectionFactory());
        return counter.decrementAndGet();
    }

    @Override
    public void setPut(String keySpace, String value) {
        setStringOperations.add(keySpace, value);
    }

    @Override
    public Set<String> setKeys(String keySpace) {
        return setStringOperations.members(keySpace);
    }

    @Override
    public long setSize(String keySpace) {
        return setStringOperations.size(keySpace);
    }

    @Override
    public String cacheProvider() {
        return REDIS;
    }

}
