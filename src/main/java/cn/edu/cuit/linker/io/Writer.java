package cn.edu.cuit.linker.io;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import java.io.IOException;

public interface Writer extends AutoCloseable, RecycleAble {
    public void write(Request request, Response response) throws IOException;
    public void close() throws IOException;
}
