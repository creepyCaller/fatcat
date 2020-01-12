package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.linker.io.Cache;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StandardCache implements Cache {

    private static Map<String, byte[]> cache;

    static {
        cache = new HashMap<>();
    }

    @Override
    public void put(String key, byte[] value) {
        cache.put(key, value);
    }

    @Override
    public byte[] get(String key) {
        return cache.get(key);
    }
}
