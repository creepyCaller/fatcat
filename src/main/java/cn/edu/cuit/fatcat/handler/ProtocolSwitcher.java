package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.io.SocketWrapper;

public enum ProtocolSwitcher {
    INSTANCE;

    public Handler getHandler(SocketWrapper socketWrapper) {
        Http11Handler handler = Http11Handler.newInstance();
        handler.setSocketWrapper(socketWrapper);
        return handler;
    }
}
