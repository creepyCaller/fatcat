package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.*;
import cn.edu.cuit.fatcat.adapter.RequestAdapter;
import cn.edu.cuit.fatcat.container.servlet.ServletMapping;
import cn.edu.cuit.fatcat.container.servlet.ServletCaller;
import cn.edu.cuit.fatcat.http.HttpConnection;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import cn.edu.cuit.fatcat.message.Request;
import cn.edu.cuit.fatcat.message.Response;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

/**
 * 对处理一次HTTP请求的子线程
 *
 * @author fpc
 * @date 2019/10/23
 * @since Fatcat 0.0.1
 */
@Slf4j
public class Http11Handler implements ProtocolHandler, RecycleAble {
    private SocketWrapper socketWrapper;
    private Request request;
    private Response response;
    private boolean fi;
    private boolean close;

    public Http11Handler(Request request, SocketWrapper socketWrapper) {
        this.request = request;
        this.socketWrapper = socketWrapper;
    }


    // TODO: 池分配
    public static Http11Handler newInstance(SocketWrapper socketWrapper, Request request) {
        return new Http11Handler(request, socketWrapper);
    }

    /**
     * 准备干活
     */
    @Override
    public void prepare() {
        response = Response.standardResponseBuilder().socketWrapper(socketWrapper).request(request).build();
        request.setResponse(response);
        socketWrapper.setRequest(request);
        socketWrapper.setResponse(response);
        fi = false;
        close = false;
    }

    private void getNextRequest() throws IOException {
        String requestContext = socketWrapper.getReader().readRequestContext(); // TODO: 异步?
        if (requestContext == null) {
            fi = true;
            return;
        }
        RequestAdapter.INSTANCE.getRequest(request, requestContext);
        response.recycle();
        if (HttpConnection.CLOSE.equals(request.getHeader(HttpHeader.CONNECTION))) {
            response.setHeader(HttpHeader.CONNECTION, HttpConnection.CLOSE);
            socketWrapper.getSocket().setKeepAlive(false);
        }
        response.setHeader(HttpHeader.CONNECTION, HttpConnection.KEEP_ALIVE);
        socketWrapper.getSocket().setKeepAlive(true);
    }

    /**
     * 干活
     */
    @Override
    public void work() throws Throwable {
        if (!fi) {
            String servletName = ServletMapping.INSTANCE.getServletName(request.getDispatchedDirection());
            if (servletName != null) {
                // Servlet容器:
                ServletCaller.INSTANCE.callServlet(request, response, servletName);
            } else {
                // 反向代理:
                socketWrapper.getWriter().write(request, response);
            }
            getNextRequest();
            socketWrapper.reuse();
        } else {
            close = true;
        }
    }

    @Override
    public boolean isClose() {
        return close;
    }

    /**
     * 收工
     */
    @Override
    public void done() {
        try {
            socketWrapper.close();
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        recycle();
    }

    /**
     * 调用此方法重置实例使实例回滚到刚实例化时候的状态
     */
    @Override
    public void recycle() {
    }
}
