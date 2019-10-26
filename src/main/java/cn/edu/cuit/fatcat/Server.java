package cn.edu.cuit.fatcat;

import cn.edu.cuit.fatcat.thread.ServerThread;

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
            System.out.println("正在监听, Port = " + port);
            int i = 0;
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("[Thread-" + ++i + "]ACCEPT: " + socket.toString());
                (new Thread(new ServerThread(socket))).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
