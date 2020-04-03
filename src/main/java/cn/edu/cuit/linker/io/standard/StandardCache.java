package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.linker.io.Cache;
import lombok.extern.slf4j.Slf4j;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class StandardCache implements Cache {

    private StandardCache() {}

    private static Cache instance;

    private static final Map<String, byte[]> cache = new HashMap<>();

    @Override
    public void put(String key, byte[] value) {
//        log.info("放入缓存: {}, 比特流长度: {}", key, value.length);
        cache.put(key, value);
    }

    @Override
    public byte[] get(String key) {
//        log.info("从缓存获取: {}", key);
        return cache.get(key);
    }

    public static Cache getInstance() {
        if (instance == null) {
            instance = new StandardCache();
        }
        return StandardCache.instance;
    }
}