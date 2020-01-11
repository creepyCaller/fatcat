package cn.edu.cuit.linker.io;

import java.io.IOException;

public interface Reader extends AutoCloseable {
    public byte[] readBinStr() throws IOException;
    public String readText() throws IOException;
    public void close() throws IOException;
}
