package cn.edu.cuit.fatcat.io;

import cn.edu.cuit.fatcat.RecycleAble;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.Socket;

@Slf4j
public class SocketWrapper implements Closeable, RecycleAble {
    private Socket socket;
    private InputStream iS;
    private OutputStream oS;
    private InputStreamReader iSR;
    private BufferedReader bR;
    private FatCatOutPutStream fatCatOutPutStream;
    private FatCatWriter fatCatWriter;
    private Request request;
    private Response response;

    public SocketWrapper(Socket socket) {
        setSocket(socket);
    }

    public InputStream getInputStream() throws IOException {
        if (iS == null) {
            iS = socket.getInputStream();
        }
        return iS;
    }

    public OutputStream getOutputStream() throws IOException {
        if (oS == null) {
            oS = socket.getOutputStream();
        }
        return oS;
    }

    public InputStreamReader getInputStreamReader() throws IOException {
        if (iSR == null) {
            iSR = new InputStreamReader(getInputStream());
        }
        return iSR;
    }

    public BufferedReader getBufferedReader() throws IOException {
        if (bR == null) {
            bR = new BufferedReader(getInputStreamReader());
        }
        return bR;
    }

    public FatCatOutPutStream getFatCatOutPutStream(int bufferSize) throws IOException {
        if (fatCatOutPutStream == null) {
            fatCatOutPutStream = new FatCatOutPutStream(getOutputStream(), bufferSize);
            fatCatOutPutStream.setResponse(response);
        }
        return fatCatOutPutStream;
    }

    public FatCatWriter getFatCatWriter() throws IOException {
        if (fatCatWriter == null) {
            fatCatWriter = new FatCatWriter(getOutputStream());
        }
        return fatCatWriter;
    }


    @Override
    public void close() throws IOException {
        log.info("Socket Connection Close: {}", socket.toString());
        socket.shutdownOutput();
        socket.shutdownInput();
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

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
