package cn.edu.cuit.linker.io.standard;

import cn.edu.cuit.linker.io.Writer;
import cn.edu.cuit.linker.message.Request;
import cn.edu.cuit.linker.message.Response;
import java.io.IOException;

public class StandardServletWriter implements Writer {
    private static final Writer singletonStandardServletWriter = new StandardServletWriter();

    public static Writer getWriter() {
        return StandardServletWriter.singletonStandardServletWriter;
    }

    @Override
    public void write(Request request, Response response) throws IOException {
        response.getPrinter().flush();
    }

    @Override
    public void close() throws IOException {
    }
}
