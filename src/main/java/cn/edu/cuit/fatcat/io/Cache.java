package cn.edu.cuit.fatcat.io;

public interface Cache {
    public void put(String key, byte[] value);
    public byte[] get(String key);
}
