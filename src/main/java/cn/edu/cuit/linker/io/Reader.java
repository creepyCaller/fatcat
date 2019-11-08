package cn.edu.cuit.linker.io;

import java.io.IOException;

public interface Reader extends AutoCloseable {
    public byte[] read() throws IOException;
    public byte[] read(String direction) throws IOException;
    public void close() throws IOException;
}
