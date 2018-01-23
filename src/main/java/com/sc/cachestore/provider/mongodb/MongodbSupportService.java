package com.sc.cachestore.provider.mongodb;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.sc.cachestore.provider.cachestore.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


@Component
public class MongodbSupportService implements CacheService {
    private static final String _ID = "_id";

    private static final int DEFAULT_SET_VALUE = 1;

    private static final String COUNTER = "COUNTER";

    private static final String COUNTER_KEY = "COUNTER_KEY";

    private static final String VALUE = "VALUE";

    private static final String MONGODB = "Mongodb";

    @Autowired
    private MongoOperations mongoOperations;

    @Override
    public void hashMapPut(String keySpace, String key, String value) {
        BasicDBObjectBuilder searchObject = BasicDBObjectBuilder.start().add(_ID, key);
        BasicDBObjectBuilder modifiedObject = BasicDBObjectBuilder.start().add(_ID, key).add(VALUE, value);
        mongoOperations.getCollection(keySpace).update(searchObject.get(), modifiedObject.get(), true, false);
    }

    @Override
    public String hashMapGet(String keySpace, String key) {
        BasicDBObjectBuilder searchObject = BasicDBObjectBuilder.start().add(_ID, key);
        BasicDBObjectBuilder projectedObject = BasicDBObjectBuilder.start().add(VALUE, 1);
        DBObject dBObject = mongoOperations.getCollection(keySpace).findOne(searchObject.get(), projectedObject.get());
        if (null != dBObject) {
            return (String) dBObject.get(VALUE);
        }
        return null;
    }

    @Override
    public Map<String, String> hashMapEntries(String keySpace) {
        Map<String, String> map = new HashMap<>();
        DBCursor dBCursor = mongoOperations.getCollection(keySpace).find();
        dBCursor.forEach((o) -> map.put((String) o.get(_ID), (String) o.get(VALUE)));
        return map;
    }

    @Override
    public Set<String> hashMapKeys(String keySpace) {
        return keys(keySpace);
    }

    private Set<String> keys(String keySpace) {
        Set<String> keys = new HashSet<>();
        BasicDBObject allQuery = new BasicDBObject();
        BasicDBObject fields = new BasicDBObject();
        fields.put(VALUE, 0);
        DBCursor dBCursor = mongoOperations.getCollection(keySpace).find(allQuery);
        dBCursor.forEach((o) -> keys.add((String) o.get(_ID)));
        return keys;
    }

    @Override
    public void setAtomicLongValue(String keySpace, Long value) {
        BasicDBObjectBuilder searchObject = BasicDBObjectBuilder.start().add(_ID, COUNTER_KEY);
        BasicDBObjectBuilder modifiedObject = BasicDBObjectBuilder.start().add(_ID, COUNTER_KEY).add(COUNTER, value);
        mongoOperations.getCollection(keySpace).findAndModify(searchObject.get(), modifiedObject.get());
    }

    @Override
    public Long getAtomicLongValue(String keySpace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long incrementAtomicLongValue(String keySpace, Long value) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Long decrementAtomicLongValue(String keySpace) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setPut(String keySpace, String value) {
        BasicDBObjectBuilder searchObject = BasicDBObjectBuilder.start().add(_ID, value);
        BasicDBObjectBuilder modifiedObject = BasicDBObjectBuilder.start().add(_ID, value).add(VALUE,
            DEFAULT_SET_VALUE);
        mongoOperations.getCollection(keySpace).update(searchObject.get(), modifiedObject.get(), true, false);
    }

    @Override
    public Set<String> setKeys(String keySpace) {
        return keys(keySpace);
    }

    @Override
    public long setSize(String keySpace) {
        return mongoOperations.getCollection(keySpace).count();
    }

    @Override
    public String cacheProvider() {
        return MONGODB;
    }

}
