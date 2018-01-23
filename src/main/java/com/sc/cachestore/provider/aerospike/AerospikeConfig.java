package com.sc.cachestore.provider.aerospike;

import com.aerospike.client.AerospikeClient;
import com.aerospike.client.policy.ClientPolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.aerospike.core.AerospikeTemplate;

@Configuration
public class AerospikeConfig {

    @Autowired
    private AerospikeProperties properties;

    @Bean(destroyMethod = "close")
    public AerospikeClient aerospikeClient() {
        ClientPolicy policy = new ClientPolicy();
        policy.failIfNotConnected = true;
        policy.user = properties.getUser();
        policy.password = properties.getPassword();
        policy.readPolicyDefault.timeout = 1000;
        policy.readPolicyDefault.maxRetries = 1;
        policy.readPolicyDefault.sleepBetweenRetries = 10;
        policy.writePolicyDefault.timeout = 2000;
        policy.writePolicyDefault.maxRetries = 1;
        policy.writePolicyDefault.sleepBetweenRetries = 50;
        //policy.writePolicyDefault.recordExistsAction = RecordExistsAction.REPLACE;

        return new AerospikeClient(policy, properties.getHostName(), properties.getPort());
    }

    @Bean
    public AerospikeTemplate aerospikeTemplate() {
        return new AerospikeTemplate(aerospikeClient(), properties.getNamespace());
    }
}
