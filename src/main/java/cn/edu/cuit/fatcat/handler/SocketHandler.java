package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.io.SocketWrapper;

import java.io.IOException;

public class SocketHandler implements Runnable {
    private SocketWrapper socketWrapper;

    private SocketHandler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void run() {
        // 从套接字读取请求对象, 调用switcher, 返回Handler后调用handle
        try {
            Handler handler = HandlerSwitcher.INSTANCE.getHandler(socketWrapper);
            handler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void newThread(SocketWrapper socketWrapper) {
        (new Thread(new SocketHandler(socketWrapper))).start();
    }
}
