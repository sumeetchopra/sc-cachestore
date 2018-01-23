package com.sc.cachestore.provider.mongodb;

import com.mongodb.MongoClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
public class MongodbConfig {
    @Autowired
    private MongodbProperties properties;

    @Bean
    public MongoClient mongoClient() throws Exception {
        MongoClient mongoClient = new MongoClient(properties.getHostName(), properties.getPort());
        return mongoClient;

    }

    @Bean
    public MongoTemplate mongoTemplate(MongoClient mongoClient) throws Exception {
        MongoTemplate mongoTemplate = null;
        if (StringUtils.isEmpty(properties.getUser())) {
            mongoTemplate = new MongoTemplate(mongoClient, properties.getDatabase());
        } else {
            UserCredentials cred = new UserCredentials(properties.getUser(), properties.getPassword());
            mongoTemplate = new MongoTemplate(mongoClient, properties.getDatabase(), cred);
        }
        return mongoTemplate;

    }
}
