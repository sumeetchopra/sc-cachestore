package com.sc.cachestore.provider.aerospike;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AerospikeProperties {
	@Value("${sc.aerospike.hostname:localhost}")
	private String hostName;

	@Value("${sc.aerospike.port:3000}")
	private int port;

	@Value("${sc.aerospike.namespace:test}")
	private String namespace;
	
	@Value("${sc.aerospike.user:}")
	private String user;
	
	@Value("${sc.aerospike.password:}")
	private String password;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

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

	public String getNamespace() {
		return namespace;
	}

	public void setNamespace(String namespace) {
		this.namespace = namespace;
	}

}
