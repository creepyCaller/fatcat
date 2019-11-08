package cn.edu.cuit.linker.io;

import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class Cache {

    private static Map<String, byte[]> cache;

    static {
        cache = new HashMap<>();
    }

    public static void put(String key, byte[] value) {
        cache.put(key, value);
        log.info("存入缓存: " + key);
    }

    public static byte[] get(String key) {
        return cache.get(key);
    }

}
