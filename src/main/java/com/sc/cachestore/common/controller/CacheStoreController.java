package com.sc.cachestore.common.controller;

import com.sc.cachestore.provider.cachestore.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;


@RestController
@RequestMapping("/cachestore")
public class CacheStoreController {
    @Autowired
    private CacheService cacheService;

    @RequestMapping(path = "/putinmap/{keySpace}", method = POST)
    public String putInMap(@PathVariable String keySpace, @RequestParam Map<String, String> keyValues) {
        keyValues.forEach((k, v) -> {
            cacheService.hashMapPut(keySpace, k, v);
        });
        return "Success";
    }

    @RequestMapping(path = "/getfrommap/{keySpace}/{key}", method = GET)
    public String putInMap(@PathVariable String keySpace, @PathVariable String key) {
        final String val = cacheService.hashMapGet(keySpace, key);
        return "The Value is - " + val;
    }

    @RequestMapping(path = "/mapentries/{keySpace}", method = GET)
    public String mapEntries(@PathVariable String keySpace) {
        final Map<String, String> data = cacheService.hashMapEntries(keySpace);
        return "The Entries in map - " + data;
    }

    @RequestMapping(path = "/mapkeys/{keySpace}", method = GET)
    public String mapKeys(@PathVariable String keySpace) {
        final Set<String> data = cacheService.hashMapKeys(keySpace);
        return "The Keys in map - " + data;
    }

    @RequestMapping(path = "/putinset/{keySpace}", method = POST)
    public String putInSet(@PathVariable String keySpace, @RequestParam("value") String value) {
        cacheService.setPut(keySpace, value);
        return "Success";
    }

    @RequestMapping(path = "/setkeys/{keySpace}", method = GET)
    public String setKeys(@PathVariable String keySpace) {
        final Set<String> data = cacheService.setKeys(keySpace);
        return "The Keys in set - " + data;
    }

    @RequestMapping(path = "/setsize/{keySpace}", method = GET)
    public String setSize(@PathVariable String keySpace) {
        return "The Size of set - " + cacheService.setSize(keySpace);
    }
}
