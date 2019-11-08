package cn.edu.cuit.linker.io;

import java.io.IOException;

public interface Writer extends AutoCloseable {
    public void write(byte[] bStr) throws IOException;
    public void close() throws IOException;
}
