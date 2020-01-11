package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import java.io.IOException;

public class ServletWriter implements Writer {

    @Override
    public void write(Request request, Response response) throws IOException {
        response.getPrinter().flush();
    }

    @Override
    public void close() throws IOException {
    }
}
