package com.sc.cachestore.provider.redis;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
	@Autowired
	private RedisProperties properties;

	@Bean
	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory();
		redisConnectionFactory.setHostName(properties.getHostName());
		redisConnectionFactory.setPort(properties.getPort());

		if (StringUtils.isNotEmpty(properties.getPassword())) {
			redisConnectionFactory.setPassword(properties.getPassword());
		}

		return redisConnectionFactory;
	}

	@Bean
	public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		StringRedisTemplate redisTemplate = new StringRedisTemplate();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		// TODO Figure out expiring time
		// redisTemplate.expire(key, timeout, unit);
		return redisTemplate;
	}

	@Bean
	public RedisTemplate<String, Long> longRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
		RedisTemplate<String, Long> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(redisConnectionFactory);
		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setHashKeySerializer(new StringRedisSerializer());
		redisTemplate.afterPropertiesSet();
		// TODO Figure out expiring time
		// redisTemplate.expire(key, timeout, unit);
		return redisTemplate;
	}

}
