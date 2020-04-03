package cn.edu.cuit.linker.io;

import cn.edu.cuit.fatcat.RecycleAble;

import java.io.IOException;

public interface Reader extends AutoCloseable, RecycleAble {
    public byte[] readBinStr() throws IOException;
    public String readText() throws IOException;
    public void close() throws IOException;
}
