package cn.edu.cuit.linker.io;

import cn.edu.cuit.linker.Server;

public interface Cache {
    public void put(String key, byte[] value);
    public byte[] get(String key);
    public static Cache getInstance() {
        return Server.cache;
    }
}
