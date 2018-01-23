package com.sc.cachestore.common.test;

import com.sc.cachestore.provider.cachestore.CacheService;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class CacheImpl implements CacheService {
    private Map<String, Map<String, String>> cache = new HashMap<>();

    private Map<String, Long> atomicMap = new ConcurrentHashMap<>();

    @Override
    public void hashMapPut(String keySpace, String key, String value) {
        if (null == cache.get(keySpace)) {
            cache.put(keySpace, new HashMap<>());
        }
        cache.get(keySpace).put(key, value);
    }

    @Override
    public String hashMapGet(String keySpace, String key) {
        Map<String, String> data = hashMapEntries(keySpace);
        if (data != null) {
            return data.get(key);
        } else {
            return null;
        }
    }

    @Override
    public Map<String, String> hashMapEntries(String keySpace) {
        return cache.get(keySpace);
    }

    @Override
    public Set<String> hashMapKeys(String keySpace) {
        Map<String, String> data = hashMapEntries(keySpace);
        if (data != null) {
            return data.keySet();
        } else {
            return null;
        }
    }

    @Override
    public void setAtomicLongValue(String keySpace, Long value) {
        atomicMap.put(keySpace, value);
    }

    @Override
    public Long getAtomicLongValue(String keySpace) {
        return atomicMap.get(keySpace);
    }

    @Override
    public Long incrementAtomicLongValue(String keySpace, Long value) {
        return opr(keySpace, value);
    }

    @Override
    public Long decrementAtomicLongValue(String keySpace) {
        return opr(keySpace, -1l);
    }

    private Long opr(String keySpace, Long value) {
        return atomicMap.put(keySpace, atomicMap.get(keySpace) + value);
    }

    @Override
    public void setPut(String keySpace, String value) {
        hashMapPut(keySpace, value, null);
    }

    @Override
    public Set<String> setKeys(String keySpace) {
        return hashMapKeys(keySpace);
    }

    @Override
    public long setSize(String keySpace) {
        return hashMapEntries(keySpace).size();
    }

    @Override
    public String cacheProvider() {
        return "In Memory";
    }
}
