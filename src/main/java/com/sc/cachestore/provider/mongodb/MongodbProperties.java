package com.sc.cachestore.provider.mongodb;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MongodbProperties {
	@Value("${sc.mongodb.hostname:localhost}")
	private String hostName;

	@Value("${sc.mongodb.port:3000}")
	private int port;

	@Value("${sc.mongodb.database:test}")
	private String database;

	@Value("${sc.mongodb.user:}")
	private String user;

	@Value("${sc.mongodb.password:}")
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

	public String getDatabase() {
		return database;
	}

	public void setDatabase(String database) {
		this.database = database;
	}

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

}
