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
public class HttpHandler implements RunnableFunctionalModule, RecycleAble {
    private SocketWrapper socketWrapper;
    private StandardReader reader;
    private boolean close;

    HttpHandler(SocketWrapper socketWrapper) {
        this.socketWrapper = socketWrapper;
    }

    /**
     * 这个线程要和分离
     * @see Thread#run()
     * TODO: http长连接
     */
    @Override
    public void run() {
        prepare();
        while (!close) {
            // TODO: 限制同一个Client建立socket的数量
            work();
        }
        done();
    }

    /**
     * 准备干活
     */
    @Override
    public void prepare() {
        // TODO: 池分配
        try {
            reader = new StandardReader(socketWrapper.getInputStream());
        } catch (IOException e) {
            log.error(e.toString());
            e.printStackTrace();
        }
        close = false; // 默认长连接
    }

    /**
     * 干活
     */
    @Override
    public void work() {
        try {
            Request request = RequestAdapter.INSTANCE.getRequest(reader.readRequestContext());
            request.setSocketWrapper(socketWrapper);
            socketWrapper.setRequest(request);
            Response response = Response.standardResponse();
            response.setSocketWrapper(socketWrapper);
            socketWrapper.setResponse(response);
            close = isClose(request, response);
            socketWrapper.getSocket().setKeepAlive(!close);
            if (close) {
                response.setHeader(HttpHeader.CONNECTION, HttpConnection.CLOSE);
            }
            if (ServletMapping.INSTANCE.getServletName(request) != null) {
                // Servlet容器:
                // TODO: Chunked
                ServletCaller.INSTANCE.callServlet(request, response);
            } else {
                // 反向代理:
                StandardWriter writer = new StandardWriter(socketWrapper.getOutputStream());
                writer.write(request, response);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    // TODO: 限制连接数，设置连接持续时间, 超时关闭
    private boolean isClose(Request request, Response response) {
//        return HttpConnection.CLOSE.equals(request.getHeader(HttpHeader.CONNECTION));
//        return true;
        return false;
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
