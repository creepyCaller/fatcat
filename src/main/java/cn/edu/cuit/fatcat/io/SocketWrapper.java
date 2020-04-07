package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.RecycleAble;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketWrapper implements Closeable, RecycleAble {
    private Socket socket;

    public SocketWrapper(Socket socket) {
        this.socket = socket;
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public PrintWriter getPrintWriter() throws IOException {
        return new PrintWriter(this.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        this.socket.close();
    }

    @Override
    public void recycle() {

    }
}
