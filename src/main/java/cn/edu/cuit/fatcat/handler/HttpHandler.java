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
    private Request request;
    private Response response;
    private SocketWrapper socketWrapper;
    private StandardReader reader;
    private StandardWriter writer;
    private Boolean close;

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
            writer = new StandardWriter(socketWrapper.getOutputStream());
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
            request = RequestAdapter.getInstance().getRequest(reader.readRequestContext());
            request.setSocketWrapper(socketWrapper);
            close = isClose();
            socketWrapper.getSocket().setKeepAlive(!close);
            response = Response.standardResponse();
            response.setSocketWrapper(socketWrapper);
            if (close) {
                response.setHeader(HttpHeader.CONNECTION, HttpConnection.CLOSE);
            }
            if (ServletMapping.getInstance().getServletName(request) != null) {
                // Servlet容器:
                // TODO: 每个请求对应的ServletCaller实例,由池分配
                // TODO: Chunked
                (new ServletCaller(request, response)).start();
            } else {
                // 反向代理:
                // TODO: Content-Length
                writer.write(request, response);
            }
        } catch (Throwable e) {
            log.error(e.toString());
            e.printStackTrace();
        }
    }

    private Boolean isClose() {
//        return HttpConnection.CLOSE.equals(request.getHeader(HttpHeader.CONNECTION)); // TODO: 连接持续时间, 超时关闭, 反正就是主动关闭socket的办法
        return true;
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
