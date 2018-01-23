package com.sc.cachestore.provider.cachestore;

import java.util.Map;
import java.util.Set;

public interface CacheService {
    String WILD_CARD = "*";

    void hashMapPut(String keySpace, String key, String value);

    String hashMapGet(String keySpace, String key);

    Map<String, String> hashMapEntries(String keySpace);

    Set<String> hashMapKeys(String keySpace);

    void setAtomicLongValue(String keySpace, Long value);

    Long getAtomicLongValue(String keySpace);

    Long incrementAtomicLongValue(String keySpace, Long value);

    Long decrementAtomicLongValue(String keySpace);

    void setPut(String keySpace, String value);

    Set<String> setKeys(String keySpace);

    long setSize(String keySpace);

    String cacheProvider();
}
