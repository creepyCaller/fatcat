package cn.edu.cuit.linker.io;

public interface Cache {
    public void put(String key, byte[] value);
    public byte[] get(String key);
}
