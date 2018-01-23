package com.sc.cachestore.common;

import com.sc.cachestore.provider.conf.CacheStoreConfig;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import static org.springframework.boot.SpringApplication.run;


@SpringBootApplication
@Import(CacheStoreConfig.class)
public class Boot {

    public static void main(String[] args) {
        run(Boot.class, args);
        System.out.println("----- Cache Store Loaded -----");
    }

}
