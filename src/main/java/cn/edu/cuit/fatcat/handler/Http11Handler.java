package cn.edu.cuit.fatcat.handler;

import cn.edu.cuit.fatcat.*;
import cn.edu.cuit.fatcat.adapter.RequestAdapter;
import cn.edu.cuit.fatcat.container.servlet.ServletMapping;
import cn.edu.cuit.fatcat.container.servlet.ServletCaller;
import cn.edu.cuit.fatcat.http.HttpConnection;
import cn.edu.cuit.fatcat.http.HttpHeader;
import cn.edu.cuit.fatcat.io.SocketWrapper;
import cn.edu.cuit.fatcat.io.io.StandardReader;
import cn.edu.cuit.fatcat.io.io.StandardWriter;
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
    private StandardReader reader;
    private StandardWriter writer;
    private Request request;
    private Response response;
    private boolean fi;
    private boolean close;

    Http11Handler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
        fi = false;
        close = false;
    }

    /**
     * 准备干活
     */
    @Override
    public void prepare() {
        // TODO: 池分配
        try {
            reader = new StandardReader(socketWrapper.getInputStream());
            writer = new StandardWriter(socketWrapper.getOutputStream());
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    private void execRequestAndResponse() throws IOException {
        String requestContext = reader.readRequestContext();
        if (requestContext == null) {
            fi = true;
            return;
        }
        request = RequestAdapter.INSTANCE.getRequest(requestContext); // TODO: 修复request会被复用的问题
        request.setSocketWrapper(socketWrapper);
        socketWrapper.setRequest(request);
        response = Response.standardResponse();
        response.setSocketWrapper(socketWrapper);
        socketWrapper.setResponse(response);
        if (HttpConnection.CLOSE.equals(request.getHeader(HttpHeader.CONNECTION))) {
            response.setHeader(HttpHeader.CONNECTION, HttpConnection.CLOSE);
            socketWrapper.getSocket().setKeepAlive(false);
        } else {
            response.setHeader(HttpHeader.CONNECTION, HttpConnection.KEEP_ALIVE);
            socketWrapper.getSocket().setKeepAlive(true);
        }
    }

    /**
     * 干活
     */
    @Override
    public void work() throws Throwable {
        execRequestAndResponse();
        if (!fi) {
            request.setDirection(Dispatcher.INSTANCE.dispatch(request.getDirection())); // 处理转发
            String servletName = ServletMapping.INSTANCE.getServletName(request.getDirection());
            if (servletName != null) {
                // Servlet容器:
                ServletCaller.INSTANCE.callServlet(request, response, servletName);
            } else {
                // 反向代理:
                writer.write(request, response);
            }
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
