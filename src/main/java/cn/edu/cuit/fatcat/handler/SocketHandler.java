package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.io.SocketWrapper;

public class SocketHandler implements Runnable {
    private SocketWrapper socketWrapper;

    private SocketHandler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    @Override
    public void run() {
        // TODO: 从套接字读取请求上下文, 调用switcher, 返回Handler后调用handle
        Handler handler = ProtocolSwitcher.INSTANCE.getHandler(socketWrapper);
        handler.handle();
    }

    public static void newThread(SocketWrapper socketWrapper) {
        (new Thread(new SocketHandler(socketWrapper))).start();
    }
}
