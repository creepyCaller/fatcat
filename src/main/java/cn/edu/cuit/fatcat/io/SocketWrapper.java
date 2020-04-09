package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.RecycleAble;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketWrapper implements Closeable, RecycleAble {
    private Socket socket;

    public SocketWrapper(Socket socket) {
        setSocket(socket);
    }

    public InputStream getInputStream() throws IOException {
        return socket.getInputStream();
    }

    public BufferedReader getBufferedReader() throws IOException {
        return new BufferedReader(new InputStreamReader(getInputStream()));
    }

    public OutputStream getOutputStream() throws IOException {
        return socket.getOutputStream();
    }

    public PrintWriter getPrintWriter() throws IOException {
        return new PrintWriter(this.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        log.info("Socket Connection Close: {}", socket.toString());
        socket.shutdownInput();
        socket.shutdownOutput();
        socket.close();
    }

    @Override
    public void recycle() {
        socket = null;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        log.info("Socket Connection Established: {}", socket.toString());
        this.socket = socket;
    }
}
