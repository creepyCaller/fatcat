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
        // 调用switcher, 返回Handler后调用handle
        try {
            Handler handler = HandlerSwitcher.INSTANCE.getHandler(socketWrapper);
            handler.handle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleSocket(SocketWrapper socketWrapper) {
        (new Thread(new SocketHandler(socketWrapper))).start();
    }
}
