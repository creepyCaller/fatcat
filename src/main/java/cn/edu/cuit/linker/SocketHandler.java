package cn.edu.cuit.linker;

import lombok.extern.slf4j.Slf4j;
import java.net.Socket;

@Slf4j
public class SocketHandler {

    private Socket socket;

    public SocketHandler(Socket socket) {
        this.socket = socket;
        log.info("建立套接字：" + socket.toString());
    }

    public Socket getSocket() {
        return socket;
    }

}
