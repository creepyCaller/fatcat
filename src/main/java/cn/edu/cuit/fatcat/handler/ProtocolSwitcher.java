package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.io.SocketWrapper;

public enum ProtocolSwitcher {
    INSTANCE;

    public Handler getHandler(SocketWrapper socketWrapper) {
        return new Http11Handler(socketWrapper);
    }
}
