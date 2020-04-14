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

    private SocketWrapper(Socket socket) {
        setSocket(socket);
    }

    public static SocketWrapper newInstance(Socket socket) {
        return new SocketWrapper(socket);
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

    public FatCatWriter getFatCatWriter(int bufferSize) throws IOException {
        if (fatCatWriter == null) {
            fatCatWriter = new FatCatWriter(getFatCatOutPutStream(bufferSize));
            fatCatWriter.setResponse(response);
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
        iS = null;
        oS = null;
        iSR = null;
        bR = null;
        fatCatOutPutStream = null;
        fatCatWriter = null;
        request = null;
        response = null;
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
        // 复用tcp连接时, 需要注意套接字包装类中的输出流或者writer中的response也要设置到
        // 如果今后需要设置request, 那么也要注意
        if (fatCatOutPutStream != null) {
            fatCatOutPutStream.setResponse(response);
        }
        if (fatCatWriter != null) {
            fatCatWriter.setResponse(response);
        }
    }

    /**
     * 重置状态flag
     */
    public void reuse() {
        if (fatCatOutPutStream != null) {
            // 如果已经实例化输出流，就重置输出流的commit状态（即打印响应头、响应行、空行的flag）, 因为上次commit后肯定设置为true了
            fatCatOutPutStream.resetCommit();
        }
    }
}
