package cn.edu.cuit.fatcat;

import lombok.extern.slf4j.Slf4j;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 使用HTTP协议，监听端口port，接收HTTP请求报文、返回HTTP响应报文
 *
 * @author  fpc
 * @date  2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Server implements Runnable {
    private int port;

    Server(int port) {
        this.port = port;
    }

    /**
     * 用来承载一个Server实例的Runnable接口实现类
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        try {
            ServerSocket serverSocket = new ServerSocket(port);
            log.info("服务器启动成功!");
            log.info("正在监听端口： " + port);
            while (true) {
                Socket socket = serverSocket.accept();
                log.info("建立连接: " + socket.toString());
                (new Thread(new GeneralServlet(socket))).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
