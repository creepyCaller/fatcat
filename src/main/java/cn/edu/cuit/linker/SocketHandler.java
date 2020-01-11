package cn.edu.cuit.linker;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketHandler implements AutoCloseable {

    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        log.info("建立套接字：" + socket.toString());
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
    public void close() throws Exception {
        this.socket.close();
    }
}
