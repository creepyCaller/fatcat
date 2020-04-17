package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.adapter.RequestAdapter;
import cn.edu.cuit.fatcat.http.HttpProtocol;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import cn.edu.cuit.fatcat.message.Request;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Slf4j
public enum HandlerSwitcher {
    INSTANCE;

    public Handler getHandler(SocketWrapper socketWrapper) throws IOException {
        Request request = Request.builder().socketWrapper(socketWrapper).build(); // 初始化request
        String requestContext = socketWrapper.getReader().readRequestContext(); // 读取请求报文
        RequestAdapter.INSTANCE.setRequest(request, requestContext); // 设置请求对象
        Handler handler;
        switch (request.getProtocol()) {
            case HttpProtocol.AJP:
                handler = null;
                break;
            case HttpProtocol.HTTP_1_1:
                handler = Http11Handler.getHandler(socketWrapper, request);
                break;
            case HttpProtocol.H2:
                handler = null;
                break;
            case HttpProtocol.H3:
                handler = null;
                break;
            default:
                handler = null;
        }
        return handler;
    }
}
