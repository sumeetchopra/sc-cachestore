package com.sc.cachestore.provider.conf;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "${sc.cachestore.packages}")
@EnableAutoConfiguration
public class CacheStoreConfig {
}
