package com.sc.cachestore.provider.redis;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RedisProperties {
	@Value("${sc.redis.hostname:localhost}")
	private String hostName;

	@Value("${sc.redis.port:6379}")
	private int port;

	@Value("${sc.redis.password:}")
	private String password;

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
