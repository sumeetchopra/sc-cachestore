package com.sc.cachestore.provider.aerospike;

import com.aerospike.client.*;
import com.aerospike.client.policy.ScanPolicy;
import com.sc.cachestore.provider.cachestore.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
public class AerospikeSupportService implements CacheService {
    private static final String COUNTER = "COUNTER";

    private static final String COUNTER_KEY = "COUNTER_KEY";

    private static final String AEROSPIKE = "Aerospike";

    private static final String VALUE = "VALUE";

    private static final String KEY = "KEY";

    private static final int DEFAULT_SET_VALUE = 1;

    @Autowired
    private AerospikeClient aerospikeClient;

    @Autowired
    private AerospikeProperties properties;

    @Override
    public void hashMapPut(String keySpace, String key, String value) {
        final Key rowKey = new Key(properties.getNamespace(), keySpace, key);
        final Bin keyBin = new Bin(KEY, key);
        final Bin valueBin = new Bin(VALUE, value);
        // Write a record
        aerospikeClient.put(null, rowKey, keyBin, valueBin);
    }

    @Override
    public String hashMapGet(String keySpace, String key) {
        final Key rowKey = new Key(properties.getNamespace(), keySpace, key);
        final Record record = aerospikeClient.get(null, rowKey, VALUE);
        if (null != record) {
            return record.getString(VALUE);
        }
        return null;
    }

    @Override
    public Map<String, String> hashMapEntries(String keySpace) {
        final RecordData recordData = new RecordData();
        final ScanPolicy policy = new ScanPolicy();
        policy.concurrentNodes = true;
        policy.includeBinData = true;
        aerospikeClient.scanAll(policy, properties.getNamespace(), keySpace, recordData);
        return recordData.records;
    }

    @Override
    public Set<String> hashMapKeys(String keySpace) {
        return retrieveKeys(keySpace);
    }

    @Override
    public void setAtomicLongValue(String keySpace, Long value) {
        final Key counterKey = new Key(properties.getNamespace(), keySpace, COUNTER_KEY);
        final Bin counterBin = new Bin(COUNTER, value);
        aerospikeClient.put(null, counterKey, counterBin);
    }

    @Override
    public Long getAtomicLongValue(String keySpace) {
        final Key counterKey = new Key(properties.getNamespace(), keySpace, COUNTER_KEY);
        final Record record = aerospikeClient.get(null, counterKey, COUNTER);
        if (null != record) {
            return record.getLong(COUNTER);
        }
        return 0l;
    }

    @Override
    public Long incrementAtomicLongValue(String keySpace, Long value) {
        return counterSetAndGet(keySpace, value);
    }

    private Long counterSetAndGet(String keySpace, Long value) {
        final Key counterKey = new Key(properties.getNamespace(), keySpace, COUNTER_KEY);
        final Bin counterBin = new Bin(COUNTER, value);
        final Record record = aerospikeClient.operate(null, counterKey, Operation.add(counterBin),
            Operation.get(COUNTER));
        if (null != record) {
            return record.getLong(COUNTER);
        }
        return 0l;
    }

    @Override
    public Long decrementAtomicLongValue(String keySpace) {
        return counterSetAndGet(keySpace, -1l);
    }

    @Override
    public void setPut(String keySpace, String value) {
        final Key rowKey = new Key(properties.getNamespace(), keySpace, value);
        final Bin keyBin = new Bin(KEY, value);
        final Bin valueBin = new Bin(VALUE, DEFAULT_SET_VALUE);
        // Write a record
        aerospikeClient.put(null, rowKey, keyBin, valueBin);
    }

    @Override
    public Set<String> setKeys(String keySpace) {
        return retrieveKeys(keySpace);
    }

    private Set<String> retrieveKeys(String keySpace) {
        final RecordKeys recordKeys = new RecordKeys();
        final ScanPolicy policy = new ScanPolicy();
        policy.concurrentNodes = true;
        policy.includeBinData = true;
        aerospikeClient.scanAll(policy, properties.getNamespace(), keySpace, recordKeys, KEY);
        return recordKeys.keys;
    }

    @Override
    public long setSize(String keySpace) {
        return countOfRecords(keySpace);
    }

    private long countOfRecords(String keySpace) {
        final CountRecords counter = new CountRecords();
        final ScanPolicy policy = new ScanPolicy();
        policy.concurrentNodes = true;
        policy.includeBinData = false;
        aerospikeClient.scanAll(policy, properties.getNamespace(), keySpace, counter);
        return counter.recordCount;
    }

    @Override
    public String cacheProvider() {
        return AEROSPIKE;
    }

    private class CountRecords implements ScanCallback {
        private long recordCount = 0;

        @Override
        public void scanCallback(Key key, Record record) throws AerospikeException {
            recordCount++;
        }

    }

    private class RecordKeys implements ScanCallback {
        private Set<String> keys = new HashSet<>();

        @Override
        public void scanCallback(Key key, Record record) throws AerospikeException {
            keys.add(record.getString(KEY));
        }

    }

    private class RecordData implements ScanCallback {
        private HashMap<String, String> records = new HashMap<>();

        @Override
        public void scanCallback(Key key, Record record) throws AerospikeException {
            records.put(record.getString(KEY), record.getString(VALUE));
        }

    }
}
