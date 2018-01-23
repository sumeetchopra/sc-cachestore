package com.sc.cachestore.common.test;

import com.sc.cachestore.provider.cachestore.CacheService;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.Assert.*;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class CacheServiceTest {
    @Autowired
    private CacheService cacheService;

    @Test
    public void testSetup() {
        System.out.println(cacheService.cacheProvider());
        assertNotNull(cacheService.cacheProvider());
    }

    private final static int RECORD_COUNT = 50;

    @Test
    public void testHashMap() {
        cacheService.hashMapPut("KEYSPACE_1", "key11", "value11");
        cacheService.hashMapPut("KEYSPACE_2", "key21", "value21");
        assertEquals("value11", cacheService.hashMapGet("KEYSPACE_1", "key11"));
        assertEquals("value21", cacheService.hashMapGet("KEYSPACE_2", "key21"));
        assertNull(cacheService.hashMapGet("KEYSPACE_21", "key11"));
        assertNull(cacheService.hashMapGet("KEYSPACE_1", "key12221"));
        assertNotNull(cacheService.hashMapEntries("KEYSPACE_1"));
        assertNotNull(cacheService.hashMapKeys("KEYSPACE_2"));
    }

    @Test
    public void testSet() {
        cacheService.setPut("KEYSPACE_4", "value41");
        assertNotNull(cacheService.setKeys("KEYSPACE_4"));
        assertTrue(cacheService.setSize("KEYSPACE_4") != 0);
    }

    public void testAtomicity() {
        final Set<Long> d = data();
        assertNotNull(d);
        assertTrue(d.size() == RECORD_COUNT);
    }

    private Set<Long> data() {
        Set<Long> data = new LinkedHashSet<>();
        cacheService.setAtomicLongValue("AtomicityTestSet", 0l);
        Thread[] t = new Thread[RECORD_COUNT];
        for (int i = 1; i <= RECORD_COUNT; i++) {
            Thread thread = new Thread(() -> data.add(cacheService.incrementAtomicLongValue("AtomicityTestSet", 1l)));
            thread.start();
            t[i - 1] = thread;
        }
        for (int i = 0; i < RECORD_COUNT; i++) {
            try {
                t[i].join();
            } catch (InterruptedException e) {
            }
        }
        return data;
    }

}
