package cn.edu.cuit.fatcat.io;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public enum Cache {
    INSTANCE;

    private Map<String, byte[]> cache = new HashMap<>();

    public void put(String key, byte[] value) {
        cache.put(key, value);
    }

    public byte[] get(String key) {
        return cache.get(key);
    }
}
